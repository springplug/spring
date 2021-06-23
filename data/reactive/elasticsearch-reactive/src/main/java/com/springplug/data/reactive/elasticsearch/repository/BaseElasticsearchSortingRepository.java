package com.springplug.data.reactive.elasticsearch.repository;

import com.zpx.data.core.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseElasticsearchSortingRepository<T,ID> extends ReactiveElasticsearchRepository<T,ID> {

    Mono<Page<T>> findAll(T t, Page page);

    Flux<T> findAll(T t);

    Mono<Page<T>> findAllById(Iterable<ID> ids, Page page);

    Mono<Page<T>> findAllById(Iterable<ID> ids,T t, Page page);

    Flux<T> findAllById(Iterable<ID> ids, Sort sort);

    Flux<T> findAllById(Iterable<ID> ids,T t, Sort sort);

    Mono<UpdateResponse> update(String id,String script);

    Mono<UpdateResponse> update(T t);
}
