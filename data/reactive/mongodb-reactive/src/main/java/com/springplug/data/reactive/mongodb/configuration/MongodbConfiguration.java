package com.springplug.data.reactive.mongodb.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongodbConfiguration {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context, MongoCustomConversions conversions) {
        MappingMongoConverter mappingConverter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, context);
        mappingConverter.setCustomConversions(conversions);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }
}
