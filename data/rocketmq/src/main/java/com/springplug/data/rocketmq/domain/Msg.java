package com.springplug.data.rocketmq.domain;


import lombok.Data;

@Data
public class Msg {

    /**
     * 一级消息：消息topic
     */
    private String topic;
    /**
     * 二级消息：消息topic对应的tags
     */
    private String tags;
    /**
     * 消息内容
     */
    private String content;
    // 省略getter/setter方法
}
