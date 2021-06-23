package com.springplug.data.elasticsearch.repository;

import com.springplug.data.core.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseElasticsearchSortingRepository<T,ID> extends ElasticsearchRepository<T,ID> {

    Page<T> findAll(T t, Page page);

    List<T> findAll(T t);

    List<T> findAll(Query query);

    T findOne(Query query);

    Page<T> findAllById(Iterable<ID> ids, Page page);

    Page<T> findAllById(Iterable<ID> ids,T t, Page page);

    List<T> findAllById(Iterable<ID> ids, Sort sort);

    List<T> findAllById(Iterable<ID> ids,T t, Sort sort);

    UpdateResponse update(String id, String script);

    UpdateResponse update(T t);
}
