package com.springplug.push.gt.client.enums;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Mybatis-plus通用枚举 ，命名规范 ：【表明】【字段名】Em
 */
public enum GtOptTypeEm {
    TYPE_AND("and","并"),
    TYPE_OR("or","或"),
    TYPE_NOT("not","非");


    GtOptTypeEm(String value, String text) {
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
    public static GtOptTypeEm create(JSONObject json) {
        for (GtOptTypeEm gtTypeEm : GtOptTypeEm.values()) {
            if (gtTypeEm.getValue().equals(json.getInteger("value"))) {
                return gtTypeEm;
            }
        }
        throw new IllegalArgumentException("没有对应的"+json.getInteger("value"));
    }
}
