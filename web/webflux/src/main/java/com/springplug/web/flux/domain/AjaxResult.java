package com.springplug.web.flux.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springplug.common.util.string.StringUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 返回状态
     */
    @JsonIgnore
    public static String STATUS_TAG = "status";


    public Object getData(){
        return this.get(DATA_TAG);
    }
    public String getCode(){
        return StringUtils.isNull(this.get(CODE_TAG))?null:this.get(CODE_TAG).toString();
    }
    public String getMsg(){
        return StringUtils.isNull(this.get(MSG_TAG))?null:this.get(MSG_TAG).toString();
    }

    /**
     * 状态类型
     */
    public enum Type
    {
        /** 成功 */
        SUCCESS(200),
        /** 警告 */
        WARN(301),
        /** 错误 */
        ERROR(500),
        AUTH(406);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     */
    public AjaxResult(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
//        if (StringUtils.isNotNull(data))
//        {
        super.put(DATA_TAG, data);
//        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg,int status,Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
//        if (StringUtils.isNotNull(data))
//        {
        super.put(DATA_TAG, data);
        super.put(STATUS_TAG, status);
//        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Mono<AjaxResult> success()
    {
        return Mono.just(AjaxResult.success("操作成功"));
    }


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> Mono<AjaxResult> success(T data)
    {
        return Mono.just(AjaxResult.success("操作成功", data));
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult warn(String msg)
    {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult warn(String msg, Object data)
    {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Mono<AjaxResult> error()
    {
        return Mono.just(AjaxResult.error("操作失败"));
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error(int status)
    {
        return AjaxResult.error("操作失败",status);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg,int status)
    {
        return AjaxResult.error(msg, status,null);
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static AjaxResult auth()
    {
        return AjaxResult.auth("请先登录", null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult auth(String msg)
    {
        return AjaxResult.auth(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(Type.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg,int status,Object data)
    {
        return new AjaxResult(Type.ERROR, msg,status,data);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult auth(String msg, Object data)
    {
        return new AjaxResult(Type.AUTH, msg, data);
    }

}
