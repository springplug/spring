package com.springplug.data.r2dbc.base.repository;

import com.springplug.data.core.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@NoRepositoryBean
public interface BaseSortingRepository<T,ID> extends R2dbcRepository<T,ID> {

    Mono<Integer> update(T t);

    Mono<Page<T>> findAll(T t, Page page);

    Flux<T> findAll(T t);

    Flux<T> findAll(T t, Sort sort);

    Mono<T> findOne(List<CriteriaDefinition> criteriaDefinitions);

    Flux<T> findAll(List<CriteriaDefinition> criteriaDefinitions);

    Mono<Page<T>> findAll(List<CriteriaDefinition> criteriaDefinitions,Page page);

    Mono<T> findOne(T t,List<CriteriaDefinition> criteriaDefinitions);

    Flux<T> findAll(T t,List<CriteriaDefinition> criteriaDefinitions);

    Mono<Page<T>> findAll(T t,List<CriteriaDefinition> criteriaDefinitions,Page page);

    List<CriteriaDefinition> getCriteriaDefinition(T t);

}
