package com.springplug.push.gt.client.enums;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/12 16:04
 */
public enum  GtConditionEm {
    // 手机类型
    PHONE_TYPE("phone_type","手机类型"),
    // 省市
    REGION("url","省市"),
    // 用户标签
    CUSTOM_TAG("payload","用户标签"),
    // 个推用户画像使用编码
    PORTRAIT("payload_custom","个推用户画像使用编码");


    GtConditionEm(String value, String text) {
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
    public static GtConditionEm create(JSONObject json) {
        for (GtConditionEm conditionEm : GtConditionEm.values()) {
            if (conditionEm.getValue().equals(json.getInteger("value"))) {
                return conditionEm;
            }
        }
        throw new IllegalArgumentException("没有对应的"+json.getInteger("value"));
    }
}
