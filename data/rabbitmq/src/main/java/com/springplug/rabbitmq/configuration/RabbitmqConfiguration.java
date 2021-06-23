package com.springplug.rabbitmq.configuration;

import com.springplug.rabbitmq.callback.ConfirmCallBackListener;
import com.springplug.rabbitmq.callback.ReturnCallBackListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    @Autowired
    ConfirmCallBackListener confirmCallBackListener;

    @Autowired
    ReturnCallBackListener returnCallBackListener;

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        CachingConnectionFactory cachingConnectionFactory=(CachingConnectionFactory)connectionFactory;
        cachingConnectionFactory.setPublisherReturns(true);
        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setConfirmCallback(confirmCallBackListener);
        rabbitTemplate.setReturnsCallback(returnCallBackListener);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
