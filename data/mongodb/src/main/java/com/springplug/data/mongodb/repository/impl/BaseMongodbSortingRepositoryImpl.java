package com.springplug.data.mongodb.repository.impl;

import com.mongodb.client.result.UpdateResult;
import com.springplug.data.core.annotation.Operator;
import com.springplug.data.core.domain.Page;
import com.springplug.data.mongodb.repository.BaseMongodbSortingRepository;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseMongodbSortingRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements BaseMongodbSortingRepository<T, ID> {

    private final MongoEntityInformation<T, ID> entityInformation;
    private final MongoOperations mongoOperations;


    public BaseMongodbSortingRepositoryImpl(MongoEntityInformation<T, ID> entityInformation, MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation=entityInformation;
        this.mongoOperations=mongoOperations;
    }

    @Override
    public <S extends T> S save(S entity) {
        return mongoOperations.insert(entity, entityInformation.getCollectionName());
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities must not be null!");
        Streamable<S> source = Streamable.of(entities);
        List<S> result = source.stream().collect(Collectors.toList());
        return  new ArrayList<>(mongoOperations.insertAll(result));
    }

    @Override
    public UpdateResult update(T t) {

        MongoPersistentProperty idProperty = mongoOperations.getConverter().getMappingContext().getPersistentEntity(t.getClass()).getIdProperty();
        Document document = new Document();
        mongoOperations.getConverter().write(t, document);
        Object id = document.get(idProperty.getFieldName());
        document.remove(idProperty.getFieldName());
        Update update = new Update();
        document.forEach((a,b)->{
            update.set(a,b);
        });

        Query query = new Query(new Criteria(idProperty.getFieldName()).is(id))
                .collation(entityInformation.getCollation());
        return mongoOperations.updateFirst(query,update,entityInformation.getCollectionName());
    }

    @Override
    public Page<T> findAll(T t, Page<T> page) {
        Assert.notNull(t, "bean must not be null!");
        Assert.notNull(page, "page must not be null!");

        Query query = getQuery(t);

        List<T> list = mongoOperations.find(getQuery(t).with(PageRequest.of(page.getCurrent(), page.getCurrent(), getSort(page))), entityInformation.getJavaType());
        long count = mongoOperations.count(query, t.getClass());
        return new Page<T>(list,page.getCurrent(),page.getSize(),count);
    }

    @Override
    public List<T> findAll(T t) {
        Assert.notNull(t, "bean must not be null!");
        return mongoOperations.find(getQuery(t),entityInformation.getJavaType());
    }

    @Override
    public List<T> findAll(Query query) {
        return mongoOperations.find(query,entityInformation.getJavaType());
    }

    @Override
    public T findOne(Query query) {
        return mongoOperations.findOne(query,entityInformation.getJavaType());
    }

    @Override
    public Page<T> findAllById(Iterable<ID> ids, Page<T> page) {
        String idKey = entityInformation.getIdAttribute();

        Query query = new Query(new Criteria(idKey).in(toCollection(ids)))
                .collation(entityInformation.getCollation());

        List<T> list = mongoOperations.find(query.with(PageRequest.of(page.getCurrent(), page.getCurrent(), getSort(page))), entityInformation.getJavaType());
        long count = mongoOperations.count(query, entityInformation.getJavaType());
        return new Page<T>(list,page.getCurrent(),page.getSize(),count);
    }

    @Override
    public Page<T> findAllById(Iterable<ID> ids, T t, Page<T> page) {
        String idKey = entityInformation.getIdAttribute();
        Query query =getQuery(t).addCriteria(new Criteria(idKey).in(toCollection(ids))).collation(entityInformation.getCollation());

        List<T> list = mongoOperations.find(query.with(PageRequest.of(page.getCurrent(), page.getCurrent(),getSort(page))),entityInformation.getJavaType());
        long count = mongoOperations.count(query,entityInformation.getJavaType());
        return new Page<T>(list,page.getCurrent(),page.getSize(),count);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids, Sort sort) {
        String idKey = entityInformation.getIdAttribute();

        Query query = new Query(new Criteria(idKey).in(toCollection(ids)))
                .collation(entityInformation.getCollation()).with(sort);
        return mongoOperations.find(query,entityInformation.getJavaType());
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids, T t, Sort sort) {
        String idKey = entityInformation.getIdAttribute();
        Query query =getQuery(t).addCriteria(new Criteria(idKey).in(toCollection(ids))).collation(entityInformation.getCollation()).with(sort);
        return mongoOperations.find(query,entityInformation.getJavaType());
    }

    private Query getQuery(T t) {
        return Optional.ofNullable(getCriteria(mongoOperations, t)).map(criterias ->  (Query)new Query(new Criteria().andOperator(criterias))).orElse(new Query());
    }

    private Criteria[] getCriteria(MongoOperations operations, T t) {

        MongoPersistentEntity<?> requiredPersistentEntity = operations.getConverter().getMappingContext().getRequiredPersistentEntity(t.getClass());
        Document document = new Document();
        operations.getConverter().write(t, document);
        List<Criteria> criterias = new ArrayList<>();
        requiredPersistentEntity.iterator().forEachRemaining(r -> {
            Optional.ofNullable(document.get(r.getFieldName())).ifPresent(i -> {
                criterias.add(getCriteria(r, i));
            });
        });
        return criterias.size() == 0 ? null : criterias.toArray(new Criteria[]{});
    }

    private Criteria getCriteria(MongoPersistentProperty r, Object value) {
        Operator operator = r.findAnnotation(Operator.class);
        String column = r.getFieldName();
        if (null == operator) {
            return Criteria.where(column).is(value);
        }
        Criteria criteria;
        Operator.Type type = operator.value();
        String columnName = StringUtils.isEmpty(operator.column()) ? column : operator.column();

        switch (type) {
            case LIKE:
                criteria = Criteria.where(columnName).regex(value.toString());
                break;
            case LIKELEFT:
                criteria = Criteria.where(columnName).regex(Pattern.compile("^.*" +value.toString()+"$"));
                break;
            case LIKERIGHT:
                criteria = Criteria.where(columnName).regex(Pattern.compile("^"+value.toString()+".*$"));
                break;
            case GT:
                criteria = Criteria.where(columnName).gt(value.toString());
                break;
            case LT:
                criteria = Criteria.where(columnName).lt(value.toString());
                break;
            case GTE:
                criteria = Criteria.where(columnName).gte(value);
                break;
            case LTE:
                criteria = Criteria.where(columnName).lte(value);
                break;
            default:
                criteria = Criteria.where(columnName).is(value);
        }
        return criteria;
    }

    public Sort getSort(Page<T> page){
        Sort sort = null;
        Optional<List> ascs = Optional.ofNullable(page.getAscs());
        Optional<List> descs = Optional.ofNullable(page.getDescs());
        if (ascs.isPresent()) {
            sort = Sort.by((String[]) ascs.get().toArray(new String[]{})).ascending();
        }
        if (descs.isPresent()) {
            if (null == sort) {
                sort = Sort.by((String[]) descs.get().toArray(new String[]{})).descending();
            } else {
                sort = sort.and(Sort.by((String[]) descs.get().toArray(new String[]{})).descending());
            }
        }
        return null == sort ? Sort.unsorted() : sort;
    }

    private static <E> Collection<E> toCollection(Iterable<E> ids) {
        return ids instanceof Collection ? (Collection<E>) ids
                : StreamUtils.createStreamFromIterator(ids.iterator()).collect(Collectors.toList());
    }
}
