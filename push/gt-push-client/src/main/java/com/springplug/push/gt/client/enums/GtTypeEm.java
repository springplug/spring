package com.springplug.push.gt.client.enums;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Mybatis-plus通用枚举 ，命名规范 ：【表明】【字段名】Em
 */
public enum GtTypeEm {
    //消息
    MESSAGE("message","消息"),
    // 通知
    NOTIFICATION("notification","通知");


    GtTypeEm(String value, String text) {
        this.value = value;
        this.text = text;
    }

    private String value;

    private String text;

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public static GtTypeEm create(JSONObject json) {
        for (GtTypeEm gtTypeEm : GtTypeEm.values()) {
            if (gtTypeEm.getValue().equals(json.getInteger("value"))) {
                return gtTypeEm;
            }
        }
        throw new IllegalArgumentException("没有对应的"+json.getInteger("value"));
    }
}
