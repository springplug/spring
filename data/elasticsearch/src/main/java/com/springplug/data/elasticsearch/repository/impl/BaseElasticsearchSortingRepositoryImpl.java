package com.springplug.data.elasticsearch.repository.impl;

import com.springplug.data.elasticsearch.repository.BaseElasticsearchSortingRepository;
import com.springplug.data.core.annotation.Operator;
import com.springplug.data.core.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseElasticsearchSortingRepositoryImpl<T, ID> extends SimpleElasticsearchRepository<T, ID> implements BaseElasticsearchSortingRepository<T, ID> {

    private final ElasticsearchOperations operations;
    private final ElasticsearchEntityInformation entityInformation;


    public BaseElasticsearchSortingRepositoryImpl(ElasticsearchEntityInformation<T, ID> entityInformation, ElasticsearchOperations operations) {
        super(entityInformation, operations);
        this.operations = operations;
        this.entityInformation = entityInformation;
    }

    @Override
    public Page<T> findAll(T t, Page page) {
        Query query = getQuery(operations, t).setPageable(PageRequest.of(page.getCurrent() - 1, page.getSize(), getSort(page)));;
        SearchPage<T> searchPage = SearchHitSupport.searchPageFor(operations.search(query, (Class<T>) t.getClass()), PageRequest.of(page.getCurrent() - 1, page.getSize(), getSort(page)));
        return new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements());
    }

    @Override
    public List<T> findAll(T t) {
        return operations.search(getQuery(operations, t), (Class<T>) t.getClass()).map(SearchHit::getContent).toList();
    }

    @Override
    public List<T> findAll(Query query) {
        return operations.search(query,(Class<T>)entityInformation.getJavaType()).map(SearchHit::getContent).toList();
    }

    @Override
    public T findOne(Query query) {
        return operations.searchOne(query,(Class<T>)entityInformation.getJavaType()).getContent();
    }

    @Override
    public Page<T> findAllById(Iterable<ID> ids, Page page) {
        SearchPage<T> searchPage = SearchHitSupport.searchPageFor(operations.search((Query) new CriteriaQuery(Criteria.where(entityInformation.getIdAttribute()).in(ids)).addSort(getSort(page)),(Class<T>) entityInformation.getJavaType()), PageRequest.of(page.getCurrent() - 1, page.getSize()));
        return new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements());

    }

    @Override
    public Page<T> findAllById(Iterable<ID> ids, T t, Page page) {
        Criteria criteria = Criteria.where(entityInformation.getIdAttribute()).in(ids);
        Optional.ofNullable(getCriteria(operations, t)).ifPresent(c -> criteria.and(c));
        SearchPage<T> searchPage = SearchHitSupport.searchPageFor(operations.search((Query) new CriteriaQuery(criteria).addSort(getSort(page)),(Class<T>) entityInformation.getJavaType()), PageRequest.of(page.getCurrent() - 1, page.getSize()));        return new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements());
    }

    @Override
    public List<T> findAllById(Iterable<ID> id, Sort sort) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(Criteria.where(entityInformation.getIdAttribute()).in(id));
        Query query = criteriaQuery.addSort(sort);
        return operations.search(query, (Class<T>) entityInformation.getJavaType()).stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<T> findAllById(Iterable<ID> id, T t, Sort sort) {
        Criteria criteria = Criteria.where(entityInformation.getIdAttribute()).in(id);
        Optional.ofNullable(getCriteria(operations, t)).ifPresent(c -> criteria.and(c));
        Query query = new CriteriaQuery(criteria).addSort(sort);
        return operations.search(query, (Class<T>) entityInformation.getJavaType()).stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public UpdateResponse update(String id, String script) {
        return operations.update(UpdateQuery.builder(id).withScript(script).build(), entityInformation.getIndexCoordinates());
    }

    @Override
    public UpdateResponse update(T t) {
        String idAttribute = entityInformation.getIdAttribute();
        Assert.notNull(idAttribute, "ID must not be null!");
        Document document = Document.create();
        operations.getElasticsearchConverter().write(t, document);
        Object id = document.get(idAttribute);
        Assert.notNull(id, "ID must not be null!");
        document.remove(idAttribute);
        return operations.update(UpdateQuery.builder(id.toString()).withDocument(document).build(), entityInformation.getIndexCoordinates());
    }

    private Criteria[] getCriteria(ElasticsearchOperations operations, T t) {

        ElasticsearchPersistentEntity<?> requiredPersistentEntity = operations.getElasticsearchConverter().getMappingContext().getRequiredPersistentEntity(t.getClass());
        Document document = Document.create();
        operations.getElasticsearchConverter().write(t, document);
        List<Criteria> criterias = new ArrayList<>();
        requiredPersistentEntity.iterator().forEachRemaining(r -> {
            Optional.ofNullable(document.get(r.getFieldName())).ifPresent(i -> {
                criterias.add(getCriteria(r, i));
            });
        });
        return criterias.size() == 0 ? null : criterias.toArray(new Criteria[]{});
    }

    private Query getQuery(ElasticsearchOperations operations, T t) {
        return Optional.ofNullable(getCriteria(operations, t)).map(criterias ->  (Query)new CriteriaQuery(new Criteria().and(criterias))).orElse(Query.findAll());
    }


    private Criteria getCriteria(ElasticsearchPersistentProperty r, Object value) {
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
                criteria = Criteria.where(columnName).contains(value.toString());
                break;
            case LIKELEFT:
                criteria = Criteria.where(columnName).endsWith(value.toString());
                break;
            case LIKERIGHT:
                criteria = Criteria.where(columnName).startsWith(value.toString());
                break;
            case GT:
                criteria = Criteria.where(columnName).greaterThan(value.toString());
                break;
            case LT:
                criteria = Criteria.where(columnName).lessThan(value.toString());
                break;
            case GTE:
                criteria = Criteria.where(columnName).greaterThanEqual(value);
                break;
            case LTE:
                criteria = Criteria.where(columnName).lessThanEqual(value);
                break;
            default:
                criteria = Criteria.where(columnName).is(value);
        }
        return criteria;
    }

    private Sort getSort(Page page) {
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

}
