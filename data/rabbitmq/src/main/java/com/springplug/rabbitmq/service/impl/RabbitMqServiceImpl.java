package com.springplug.rabbitmq.service.impl;

import com.springplug.rabbitmq.domain.Msg;
import com.springplug.rabbitmq.service.RabbitMqService;
import com.springplug.common.util.string.StringUtils;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitTemplate template;

    @Override
    public <T> T send(Msg msg, Class<T> t) {
        return template.convertSendAndReceiveAsType(StringUtils.isEmpty(msg.getExchange())?"topic":msg.getExchange(),msg.getRoutingKey(),msg.getContent(),ParameterizedTypeReference.forType(t));
    }

    @Override
    public void asyncSend(Msg msg, MessagePostProcessor messagePostProcessor) {
        template.convertAndSend(StringUtils.isEmpty(msg.getExchange())?"topic":msg.getExchange(),msg.getRoutingKey(),msg.getContent(),messagePostProcessor);
    }

    @Override
    public void syncSendOrderly(Msg msg) {
        template.convertAndSend(StringUtils.isEmpty(msg.getExchange())?"topic":msg.getExchange(),msg.getRoutingKey(),msg.getContent());
    }
}
