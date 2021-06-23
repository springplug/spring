package com.springplug.data.reactive.redis.lock.configuration;

import com.springplug.data.reactive.redis.lock.ReactiveRedisLock;
import com.springplug.data.reactive.redis.properties.ReactiveRedisLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

/**
 * ReactiveRedisLockRegistry AutoConfiguration
 * @author: chenggang
 * @date 2021-03-14.
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
@ConditionalOnClass({ReactiveRedisLock.class, RedisReactiveAutoConfiguration.class, ReactiveRedisConnectionFactory.class, Flux.class})
public class ReactiveRedisLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ReactiveRedisLockProperties.class)
    @ConfigurationProperties(ReactiveRedisLockProperties.REDIS_LOCK_PROPERTIES_PREFIX)
    public ReactiveRedisLockProperties redisDistributedLockProperties(){
        return new ReactiveRedisLockProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ReactiveRedisLock.class)
    public ReactiveRedisLock redisLockRegistry(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, ReactiveRedisLockProperties redisDistributedLockProperties){
        String keyPrefix = StringUtils.isEmpty(redisDistributedLockProperties.getRegistryKeyPrefix()) ? "REDIS_DISTRIBUTED_LOCK" : redisDistributedLockProperties.getRegistryKeyPrefix();
        ReactiveRedisLock reactiveRedisDistributedLockRegistry = new ReactiveRedisLock(reactiveRedisConnectionFactory, keyPrefix, redisDistributedLockProperties.getExpireAfter(),redisDistributedLockProperties.getExpireAfter());
        log.info("Load Reactive Redis Distributed Lock Registry Success,RegistryKey Prefix:{},Default Expire Duration:{}",keyPrefix,redisDistributedLockProperties.getExpireAfter());
        return reactiveRedisDistributedLockRegistry;
    }

}
