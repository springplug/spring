package com.springplug.data.reactive.mongodb.repository.impl;

import com.springplug.data.reactive.mongodb.repository.BaseMongodbSortingRepository;
import com.zpx.common.util.string.StringUtils;
import com.zpx.data.core.annotation.Operator;
import com.zpx.data.core.domain.Page;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import org.springframework.data.util.StreamUtils;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseMongodbSortingRepositoryImpl<T, ID extends Serializable> extends SimpleReactiveMongoRepository<T, ID> implements BaseMongodbSortingRepository<T, ID> {

    private final MongoEntityInformation<T, ID> entityInformation;
    private final ReactiveMongoOperations mongoOperations;


    public BaseMongodbSortingRepositoryImpl(MongoEntityInformation<T, ID> entityInformation, ReactiveMongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation=entityInformation;
        this.mongoOperations=mongoOperations;
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        return mongoOperations.insert(entity, entityInformation.getCollectionName());
    }

    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities must not be null!");
        return  mongoOperations.insertAll(Flux.fromIterable(entities).collectList(), entityInformation.getCollectionName());
    }

    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> entityStream) {
        Assert.notNull(entityStream, "The given Publisher of entities must not be null!");
        return mongoOperations.insertAll(Flux.from(entityStream).collectList(), entityInformation.getCollectionName());
    }

    @Override
    public Mono<T> update(T t) {

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
        return mongoOperations.updateFirst(query,update,entityInformation.getCollectionName()).flatMap(r->Mono.just(t));
    }

    @Override
    public Mono<Page<T>> findAll(T t, Page<T> page) {
        Assert.notNull(t, "bean must not be null!");
        Assert.notNull(page, "page must not be null!");

        Query query = getQuery(t);
        return mongoOperations.find(getQuery(t).with(PageRequest.of(page.getCurrent(), page.getCurrent(),getSort(page))),entityInformation.getJavaType()).collectList().zipWith(mongoOperations.count(query,t.getClass())).flatMap(a->Mono.just(new Page<T>(a.getT1(),page.getCurrent(),page.getSize(),a.getT2())));
    }

    @Override
    public Flux<T> findAll(T t) {
        Assert.notNull(t, "bean must not be null!");
        return mongoOperations.find(getQuery(t),entityInformation.getJavaType());
    }

    @Override
    public Mono<Page<T>> findAllById(Iterable<ID> ids, Page<T> page) {
        String idKey = entityInformation.getIdAttribute();

        Query query = new Query(new Criteria(idKey).in(toCollection(ids)))
                .collation(entityInformation.getCollation());

        return mongoOperations.find(query.with(PageRequest.of(page.getCurrent(), page.getCurrent(),getSort(page))),entityInformation.getJavaType()).collectList().zipWith(mongoOperations.count(query,entityInformation.getJavaType())).flatMap(a->Mono.just(new Page<T>(a.getT1(),page.getCurrent(),page.getSize(),a.getT2())));
    }

    @Override
    public Mono<Page<T>> findAllById(Iterable<ID> ids, T t, Page<T> page) {
        String idKey = entityInformation.getIdAttribute();
        Query query =getQuery(t).addCriteria(new Criteria(idKey).in(toCollection(ids))).collation(entityInformation.getCollation());
        return mongoOperations.find(query.with(PageRequest.of(page.getCurrent(), page.getCurrent(),getSort(page))),entityInformation.getJavaType()).collectList().zipWith(mongoOperations.count(query,entityInformation.getJavaType())).flatMap(a->Mono.just(new Page<T>(a.getT1(),page.getCurrent(),page.getSize(),a.getT2())));
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> ids, Sort sort) {
        String idKey = entityInformation.getIdAttribute();

        Query query = new Query(new Criteria(idKey).in(toCollection(ids)))
                .collation(entityInformation.getCollation()).with(sort);
        return mongoOperations.find(query,entityInformation.getJavaType());
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> ids, T t, Sort sort) {
        String idKey = entityInformation.getIdAttribute();
        Query query =getQuery(t).addCriteria(new Criteria(idKey).in(toCollection(ids))).collation(entityInformation.getCollation()).with(sort);
        return mongoOperations.find(query,entityInformation.getJavaType());
    }

    private Query getQuery(T t) {
        return Optional.ofNullable(getCriteria(t)).map(criterias ->  (Query)new Query(new Criteria().andOperator(criterias))).orElse(new Query());
    }

    private Criteria[] getCriteria(T t) {

        MongoPersistentEntity<?> requiredPersistentEntity = mongoOperations.getConverter().getMappingContext().getRequiredPersistentEntity(t.getClass());
        Document document = new Document();
        mongoOperations.getConverter().write(t, document);
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
        Sort sort = Sort.unsorted();
        Optional.ofNullable(page.getAscs()).ifPresent(a -> sort.and(Sort.by(a.toArray(new String[]{}))));
        Optional.ofNullable(page.getDescs()).map(d -> sort.and(Sort.by(d.toArray(new String[]{})).descending()));
        return sort;
    }

    private static <E> Collection<E> toCollection(Iterable<E> ids) {
        return ids instanceof Collection ? (Collection<E>) ids
                : StreamUtils.createStreamFromIterator(ids.iterator()).collect(Collectors.toList());
    }
}
