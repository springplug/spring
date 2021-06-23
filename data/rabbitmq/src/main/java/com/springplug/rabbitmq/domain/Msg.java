package com.springplug.rabbitmq.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Msg {

    private String exchange;

    private String routingKey;
    /**
     * 消息内容
     */
    private Object content;
}
