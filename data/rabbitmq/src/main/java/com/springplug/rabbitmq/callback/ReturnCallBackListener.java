package com.springplug.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReturnCallBackListener implements RabbitTemplate.ReturnsCallback {

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("return--message:"+new String(returned.getMessage().getBody())+",replyCode:"+returned.getReplyCode()+",replyText:"+returned.getReplyText()+",exchange:"+returned.getExchange()+",routingKey:"+returned.getRoutingKey());
    }
}
