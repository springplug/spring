package com.springplug.data.reactive.mongodb.repository;

import com.zpx.data.core.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseMongodbSortingRepository<T,ID> extends ReactiveMongoRepository<T,ID> {

    Mono<T> update(T t);

    Mono<Page<T>> findAll(T t, Page<T> page);

    Flux<T> findAll(T t);

    Mono<Page<T>> findAllById(Iterable<ID> ids, Page<T> page);

    Mono<Page<T>> findAllById(Iterable<ID> ids, T t, Page<T> page);

    Flux<T> findAllById(Iterable<ID> ids, Sort sort);

    Flux<T> findAllById(Iterable<ID> ids, T t, Sort sort);
}
