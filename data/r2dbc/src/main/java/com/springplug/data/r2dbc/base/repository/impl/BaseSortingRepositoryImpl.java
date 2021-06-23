package com.springplug.data.r2dbc.base.repository.impl;

import com.springplug.common.util.string.StringUtils;
import com.springplug.data.core.annotation.Operator;
import com.springplug.data.core.domain.Page;
import com.springplug.data.r2dbc.base.repository.BaseSortingRepository;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.data.util.ProxyUtils;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Transactional(readOnly = true)
public class BaseSortingRepositoryImpl<T, ID> extends SimpleR2dbcRepository<T, ID> implements BaseSortingRepository<T, ID> {

    private final R2dbcEntityOperations entityOperations;
    private final R2dbcConverter converter;
    private final RelationalEntityInformation entity;


    public BaseSortingRepositoryImpl(RelationalEntityInformation entity, R2dbcEntityOperations entityOperations, R2dbcConverter converter) {
        super(entity, entityOperations, converter);
        this.entityOperations = entityOperations;
        this.converter = converter;
        this.entity = entity;
    }

    @Transactional
    @Override
    public <S extends T> Mono<S> save(S objectToSave) {
        return entityOperations.insert(objectToSave);
    }

    @Transactional
    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> objectsToSave) {
        return Flux.fromIterable(objectsToSave).concatMap(this::save);
    }

    @Transactional
    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> objectsToSave) {
        return Flux.from(objectsToSave).concatMap(this::save);
    }

    @Transactional
    @Override
    public Mono<Integer> update(T t) {
        ReactiveDataAccessStrategy dataAccessStrategy = entityOperations.getDataAccessStrategy();
        OutboundRow sourceOutboundRow = dataAccessStrategy.getOutboundRow(t);
        OutboundRow outboundRow = new OutboundRow();
        sourceOutboundRow.forEach((k, v) -> {
            if (null != v.getValue()) {
                outboundRow.put(k, v);
            }
        });
        Class<?> entityType = ProxyUtils.getUserClass(t);
        RelationalPersistentEntity<?> requiredPersistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entityType);
        SqlIdentifier idColumn = requiredPersistentEntity.getIdColumn();
        Parameter id = outboundRow.remove(idColumn);
        Criteria criteria = Criteria.where(dataAccessStrategy.toSql(idColumn)).is(id);
        Update update = Update.from((Map) outboundRow);
        return entityOperations.update(Query.query(criteria), update, t.getClass());
    }

    @Override
    public Mono<Page<T>> findAll(T t, Page page) {
        Query query = getQuery(t);
        return entityOperations.select(query.sort(getSort(page)).limit(page.getSize()).offset((page.getCurrent() - 1) * page.getSize()), (Class<T>) t.getClass()).collectList().zipWith(entityOperations.count(query, t.getClass())).flatMap(v -> Mono.just(new Page<T>(v.getT1(), page.getCurrent(),page.getSize(), v.getT2().longValue())));
    }

    @Override
    public Flux<T> findAll(T t) {
        return entityOperations.select(getQuery(t),(Class<T>) t.getClass());
    }

    @Override
    public Flux<T> findAll(T t, Sort sort) {
        return entityOperations.select(getQuery(t).sort(sort),(Class<T>) t.getClass());
    }

    @Override
    public Mono<T> findOne(List<CriteriaDefinition> criteriaDefinitions) {
        return entityOperations.selectOne(getQuery(criteriaDefinitions),entity.getJavaType());
    }

    @Override
    public Flux<T> findAll(List<CriteriaDefinition> criteriaDefinitions) {
        return entityOperations.select(getQuery(criteriaDefinitions),entity.getJavaType());
    }

    @Override
    public Mono<Page<T>> findAll(List<CriteriaDefinition> criteriaDefinitions, Page page) {
        Query query = getQuery(criteriaDefinitions);
        return entityOperations.select(query.sort(getSort(page)).limit(page.getSize()).offset((page.getCurrent() - 1) * page.getSize()), (Class<T>) entity.getJavaType()).collectList().zipWith(entityOperations.count(query,entity.getJavaType())).flatMap(v -> Mono.just(new Page<T>(v.getT1(), page.getCurrent(),page.getSize(), v.getT2().longValue())));
    }

    @Override
    public Mono<T> findOne(T t, List<CriteriaDefinition> criteriaDefinitions) {
        List<CriteriaDefinition> criteriaDefinition = getCriteriaDefinition(t);
        criteriaDefinition.addAll(criteriaDefinitions);
        return entityOperations.selectOne(getQuery(criteriaDefinition),entity.getJavaType());
    }

    @Override
    public Flux<T> findAll(T t, List<CriteriaDefinition> criteriaDefinitions) {
        List<CriteriaDefinition> criteriaDefinition = getCriteriaDefinition(t);
        criteriaDefinition.addAll(criteriaDefinitions);
        return entityOperations.select(getQuery(criteriaDefinition),entity.getJavaType());
    }

    @Override
    public Mono<Page<T>> findAll(T t, List<CriteriaDefinition> criteriaDefinitions, Page page) {
        List<CriteriaDefinition> criteriaDefinition = getCriteriaDefinition(t);
        criteriaDefinition.addAll(criteriaDefinitions);
        Query query = getQuery(criteriaDefinition);
        return entityOperations.select(query.sort(getSort(page)).limit(page.getSize()).offset((page.getCurrent() - 1) * page.getSize() ), (Class<T>) entity.getJavaType()).collectList().zipWith(entityOperations.count(query,entity.getJavaType())).flatMap(v -> Mono.just(new Page<T>(v.getT1(), page.getCurrent(),page.getSize(), v.getT2().longValue())));
    }


    private Query getQuery(T t){
        return Query.query(CriteriaDefinition.from(getCriteriaDefinition(t)));
    }

    private Query getQuery(List<CriteriaDefinition> criteriaDefinitions){
        return Query.query(CriteriaDefinition.from(criteriaDefinitions));
    }


    @Override
    public List<CriteriaDefinition> getCriteriaDefinition(T t){
        ReactiveDataAccessStrategy dataAccessStrategy = entityOperations.getDataAccessStrategy();
        OutboundRow sourceOutboundRow = dataAccessStrategy.getOutboundRow(t);
        List<CriteriaDefinition> criterias = new ArrayList<>();
        // 获取实体类属性
        RelationalPersistentEntity<?> requiredPersistentEntity = entityOperations.getConverter().getMappingContext().getRequiredPersistentEntity(t.getClass());
        // 遍历获取注解，进行查询(like;is....)
        requiredPersistentEntity.iterator().forEachRemaining(r->{
            Optional.ofNullable(sourceOutboundRow.get(r.getColumnName()).getValue()).ifPresent(i->{
                criterias.add(getCriteria(r,dataAccessStrategy,i));
            });
        });
        return criterias;
    }



    private Criteria getCriteria(RelationalPersistentProperty r, ReactiveDataAccessStrategy dataAccessStrategy,Object value){

        String column = dataAccessStrategy.toSql(r.getColumnName());
        Operator operator = r.findAnnotation(Operator.class);
        if(null==operator){
            return Criteria.where(column).is(value);
        }
        Criteria criteria;
        Operator.Type type = operator.value();
        String columnName = StringUtils.isEmpty(operator.column())?column:operator.column();

        switch (type){
            case LIKE:
                criteria=Criteria.where(columnName).like("%"+value+"%");
                break;
            case LIKELEFT:
                criteria=Criteria.where(columnName).like("%"+value);
                break;
            case LIKERIGHT:
                criteria=Criteria.where(columnName).like(value+"%");
                break;
            case GT:
                criteria=Criteria.where(columnName).greaterThan(value);
                break;
            case LT:
                criteria=Criteria.where(columnName).lessThan(value);
                break;
            case GTE:
                criteria=Criteria.where(columnName).greaterThanOrEquals(value);
                break;
            case LTE:
                criteria=Criteria.where(columnName).lessThanOrEquals(value);
                break;
            default:
                criteria=Criteria.where(columnName).is(value);
        }
        return criteria;
    }

    private Sort getSort(Page page){
        Sort sort=Sort.unsorted();
        Optional<List> ascs = Optional.ofNullable(page.getAscs());
        Optional<List> descs = Optional.ofNullable(page.getDescs());
        if(ascs.isPresent()){
            sort=Sort.by((String[])ascs.get().toArray(new String[]{})).ascending();
        }
        if(descs.isPresent()){
            if(null==sort){
                sort=Sort.by((String[])descs.get().toArray(new String[]{})).descending();
            }else{
                sort=sort.and(Sort.by((String[])descs.get().toArray(new String[]{})).descending());
            }
        }
        return null==sort?Sort.unsorted():sort;
    }
}
