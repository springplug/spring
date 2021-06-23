package com.springplug;

import com.springplug.data.elasticsearch.repository.impl.BaseElasticsearchSortingRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(repositoryBaseClass= BaseElasticsearchSortingRepositoryImpl.class)
public class ElasticsearchConfiguration {
}
