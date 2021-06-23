package com.springplug;

import com.springplug.data.r2dbc.base.repository.impl.BaseSortingRepositoryImpl;
import com.springplug.data.r2dbc.converter.EnumWriteConverter;
import com.springplug.data.r2dbc.converter.MappingR2dbcConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcRepositories(repositoryBaseClass= BaseSortingRepositoryImpl.class)
public class R2dbcConfiguration {

    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new EnumWriteConverter());
        return new R2dbcCustomConversions(converters);
    }

    @Bean
    @Primary
    public MappingR2dbcConverter r2dbcConverter(R2dbcMappingContext mappingContext,
                                                R2dbcCustomConversions r2dbcCustomConversions) {
        return new MappingR2dbcConverter(mappingContext, r2dbcCustomConversions);
    }
}
