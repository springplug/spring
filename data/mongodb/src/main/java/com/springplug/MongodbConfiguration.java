package com.springplug;

import com.springplug.data.mongodb.repository.impl.BaseMongodbSortingRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(repositoryBaseClass= BaseMongodbSortingRepositoryImpl.class)
public class MongodbConfiguration {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context, MongoCustomConversions conversions) {
        MappingMongoConverter mappingConverter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, context);
        mappingConverter.setCustomConversions(conversions);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }
}
