package com.springplug.data.redis.configuration;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 	功能描述：redis配置
 * 
 * @Package: com.ztxh.yzs.common.config
 * @author: zhangpanxiang
 * @date: 2019年5月20日 上午10:05:36
 */
@Configuration
public class RedisConfiguration {

	@Bean
	@SuppressWarnings("all")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

		template.setConnectionFactory(factory);

		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper om = new ObjectMapper();

		om.setVisibility(PropertyAccessor.ALL, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);

		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

		jackson2JsonRedisSerializer.setObjectMapper(om);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

		// key采用String的序列化方式

		template.setKeySerializer(stringRedisSerializer);

		// hash的key也采用String的序列化方式

		template.setHashKeySerializer(stringRedisSerializer);

		// value序列化方式采用jackson

		template.setValueSerializer(jackson2JsonRedisSerializer);

		// hash的value序列化方式采用jackson

		template.setHashValueSerializer(jackson2JsonRedisSerializer);

		template.afterPropertiesSet();

		return template;

	}
}
