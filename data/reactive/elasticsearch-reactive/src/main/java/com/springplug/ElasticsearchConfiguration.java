package com.springplug;

import com.springplug.data.reactive.elasticsearch.repository.impl.BaseElasticsearchSortingRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@EnableReactiveElasticsearchRepositories(repositoryBaseClass= BaseElasticsearchSortingRepositoryImpl.class)
public class ElasticsearchConfiguration {
}
