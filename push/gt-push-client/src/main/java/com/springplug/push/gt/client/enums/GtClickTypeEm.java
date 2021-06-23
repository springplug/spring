package com.springplug.push.gt.client.enums;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/12 15:39
 */
public enum  GtClickTypeEm {
    // 打开应用
    INTENT("intent","打开应用内特定页面"),
    // 打开activity
    URL("url","打开网页地址"),
    // 自定义内容
    PAYLOAD("payload","自定义消息内容启动应用"),
    // 跳转到URL
    PAYLOAD_CUSTOM("payload_custom","自定义消息内容不启动应用"),
    // 自定义内容
    STARTAPP("startapp","打开应用首页"),
    NONE("none","纯通知，无后续动作");


    GtClickTypeEm(String value, String text) {
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
    public static GtClickTypeEm create(JSONObject json) {
        for (GtClickTypeEm clickTypeEm : GtClickTypeEm.values()) {
            if (clickTypeEm.getValue().equals(json.getInteger("value"))) {
                return clickTypeEm;
            }
        }
        throw new IllegalArgumentException("没有对应的"+json.getInteger("value"));
    }
}
