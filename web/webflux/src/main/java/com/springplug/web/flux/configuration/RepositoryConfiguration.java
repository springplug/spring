package com.springplug.web.flux.configuration;

import com.springplug.web.flux.holder.ReactiveRequestContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing
public class RepositoryConfiguration {
    /**
     * 相当于实现ReactiveAuditorAware，这里是给@CreatedBy赋值的
     * <p>创建时间和修改时间，r2dbc底层会自动去判断并生成</p>
     * @return
     */
    @Bean
    ReactiveAuditorAware<Long> auditorAware() {
        return () -> ReactiveRequestContextHolder.getRequest().flatMap(request-> Mono.justOrEmpty(request.getHeaders().getFirst("Authorization-id"))).flatMap(id-> Mono.just(Long.parseLong(id)));
    }
}
