package com.springplug.data.mongodb.repository;

import com.mongodb.client.result.UpdateResult;
import com.springplug.data.core.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;

@NoRepositoryBean
public interface BaseMongodbSortingRepository<T,ID> extends MongoRepository<T,ID> {

    UpdateResult update(T t);

    Page<T> findAll(T t, Page<T> page);

    List<T> findAll(T t);

    List<T> findAll(Query query);

    T findOne(Query query);

    Page<T> findAllById(Iterable<ID> ids, Page<T> page);

    Page<T> findAllById(Iterable<ID> ids, T t, Page<T> page);

    List<T> findAllById(Iterable<ID> ids, Sort sort);

    List<T> findAllById(Iterable<ID> ids, T t, Sort sort);
}
