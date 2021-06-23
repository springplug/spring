package com.springplug.data.rocketmq.service.impl;

import com.springplug.data.rocketmq.domain.Msg;
import com.springplug.data.rocketmq.service.RocketMqService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class RocketMqServiceImpl implements RocketMqService {

    @Autowired

    private RocketMQTemplate rocketMQTemplate;


    @Override
    public SendResult send(Msg msg) {
        return rocketMQTemplate.syncSend(msg.getTopic() + ":" + msg.getTags(), MessageBuilder.withPayload(msg.getContent()).build());
    }

    @Override
    public <T> T send(Msg msg, Class<T> t) {
        return rocketMQTemplate.sendAndReceive(msg.getTopic() + ":" + msg.getTags(),msg.getContent(),t);
    }

    @Override
    public void asyncSend(Msg msg,RocketMQLocalRequestCallback callback) {
        rocketMQTemplate.sendAndReceive(msg.getTopic() + ":" + msg.getTags(),msg.getContent(),callback);
    }

    @Override
    public void syncSendOrderly(Msg msg) {
        rocketMQTemplate.sendOneWay(msg.getTopic() + ":" + msg.getTags(),msg.getContent());
    }
}
