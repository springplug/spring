package com.springplug.app.syntony.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-24 17:13
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(20*1000);
        requestFactory.setReadTimeout(20*1000);
        return new RestTemplate(requestFactory);
    }

    @LoadBalanced
    @Bean
    public RestTemplate loadBalanced() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(20*1000);
        requestFactory.setReadTimeout(20*1000);
        return new RestTemplate(requestFactory);
    }
}
