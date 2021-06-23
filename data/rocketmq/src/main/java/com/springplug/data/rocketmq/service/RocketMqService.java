package com.springplug.data.rocketmq.service;

import com.springplug.data.rocketmq.domain.Msg;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;

public interface RocketMqService {

    /**
     * 同步发送消息<br/>
     *
     * @param msg 发送消息实体类
     */
    SendResult send(Msg msg);

    /**
     * 同步发送消息<br/>
     *
     * @param msg 发送消息实体类
     * @return 返回的具体类型
     */
    <T> T send(Msg msg, Class<T> t);

    /**
     * 异步发送消息，异步返回消息结果<br/>
     * <p>
     * 当发送的消息很重要，且对响应时间非常敏感的时候采用async方式；
     *
     * @param msg 发送消息实体类
     * @param callback 可接收具体返回信息
     */
    void asyncSend(Msg msg, RocketMQLocalRequestCallback callback);

    /**
     * 直接发送发送消息，不关心返回结果，容易消息丢失，适合日志收集、不精确统计等消息发送;
     *
     * @param msg 发送消息实体类
     */
    void syncSendOrderly(Msg msg);
}
