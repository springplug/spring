package com.springplug.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmCallBackListener implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("confirm--:correlationData:"+correlationData+",ack:"+ack+",cause:"+cause);
        }else{
            log.error("confirm--:correlationData:"+correlationData+",ack:"+ack+",cause:"+cause);
        }
    }
}
