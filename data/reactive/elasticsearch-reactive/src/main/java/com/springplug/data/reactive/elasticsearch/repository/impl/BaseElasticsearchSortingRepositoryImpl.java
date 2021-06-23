package com.springplug.data.reactive.elasticsearch.repository.impl;

import com.zpx.data.core.annotation.Operator;
import com.zpx.data.core.domain.Page;
import com.springplug.data.reactive.elasticsearch.repository.BaseElasticsearchSortingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleReactiveElasticsearchRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseElasticsearchSortingRepositoryImpl<T, ID> extends SimpleReactiveElasticsearchRepository<T, ID> implements BaseElasticsearchSortingRepository<T, ID> {

    private final ReactiveElasticsearchOperations operations;
    private final ElasticsearchEntityInformation entityInformation;


    public BaseElasticsearchSortingRepositoryImpl(ElasticsearchEntityInformation<T, ID> entityInformation, ReactiveElasticsearchOperations operations) {
        super(entityInformation, operations);
        this.operations = operations;
        this.entityInformation = entityInformation;
    }

    @Override
    public Mono<Page<T>> findAll(T t, Page page) {
        Query query = getQuery(operations, t).setPageable(PageRequest.of(page.getCurrent()-1, page.getSize(), getSort(page)));
        return this.operations.searchForPage(query, (Class<T>) t.getClass()).flatMap(searchPage ->
                Mono.just(new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements()))
        );
    }

    @Override
    public Flux<T> findAll(T t) {
        return operations.search(getQuery(operations, t), (Class<T>) t.getClass()).map(SearchHit::getContent);
    }

    @Override
    public Mono<Page<T>> findAllById(Iterable<ID> ids, Page page) {
        return this.operations.searchForPage(new CriteriaQuery(Criteria.where(entityInformation.getIdAttribute()).in(ids)).setPageable(PageRequest.of(page.getCurrent() - 1, page.getSize(),getSort(page))), (Class<T>) entityInformation.getJavaType()).flatMap(searchPage ->
                Mono.just(new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements()))
        );
    }

    @Override
    public Mono<Page<T>> findAllById(Iterable<ID> ids, T t, Page page) {
        Criteria criteria = Criteria.where(entityInformation.getIdAttribute()).in(ids);
        Optional.ofNullable(getCriteria(operations, t)).ifPresent(c -> criteria.and(c));
        return this.operations.searchForPage(new CriteriaQuery(criteria).setPageable(PageRequest.of(page.getCurrent() - 1, page.getSize(), getSort(page))), (Class<T>) entityInformation.getJavaType()).flatMap(searchPage ->
                Mono.just(new Page<T>(searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList()), searchPage.getPageable().getPageNumber(), searchPage.getPageable().getPageSize(), searchPage.getTotalElements()))
        );
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> id, Sort sort) {
        return Flux.from(Flux.fromIterable(id))
                .map(ID::toString)
                .collectList()
                .map(ids -> new NativeSearchQueryBuilder().withIds(ids).build())
                .flatMapMany(query -> {
                    IndexCoordinates index = entityInformation.getIndexCoordinates();
                    query.addSort(sort);
                    return operations.multiGet(query, (Class<T>) entityInformation.getJavaType(), index);
                });
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> id, T t, Sort sort) {
        return Flux.from(Flux.fromIterable(id))
                .map(ID::toString)
                .collectList()
                .map(ids -> Criteria.where(entityInformation.getIdAttribute()).in(ids))
                .flatMapMany(criteria -> {
                    IndexCoordinates index = entityInformation.getIndexCoordinates();
                    Optional.ofNullable(getCriteria(operations, t)).ifPresent(c -> criteria.and(c));
                    return operations.multiGet(new CriteriaQuery(criteria).addSort(sort), (Class<T>) entityInformation.getJavaType(), index);
                });
    }

    @Override
    public Mono<UpdateResponse> update(String id, String script) {
        return operations.update(UpdateQuery.builder(id).withScript(script).build(), entityInformation.getIndexCoordinates());
    }

    @Override
    public Mono<UpdateResponse> update(T t) {
        String idAttribute = entityInformation.getIdAttribute();
        Assert.notNull(idAttribute, "ID must not be null!");
        Document document = Document.create();
        operations.getElasticsearchConverter().write(t, document);
        Object id = document.get(idAttribute);
        Assert.notNull(id, "ID must not be null!");
        document.remove(idAttribute);
        return operations.update(UpdateQuery.builder(id.toString()).withDocument(document).build(), entityInformation.getIndexCoordinates());
    }

    private Criteria[] getCriteria(ReactiveElasticsearchOperations operations, T t) {
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

    private Query getQuery(ReactiveElasticsearchOperations operations, T t) {
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
