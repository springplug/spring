package com.springplug.push.gt.client.enums;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Mybatis-plus通用枚举 ，命名规范 ：【表明】【字段名】Em
 */
public enum GtCastTypeEm {
    //单推
    UNI_CAST("unicast","单推"),
    // 批量推
    LIST_CAST("listcast","批量推"),
    //
    ALL_CAST("allcast","批量推"),
    // 条件推
    CONDITION_CAST("broadcast","条件推"),
    // 标签推
    TAG_CAST("tagcast","标签推");


    GtCastTypeEm(String value, String text) {
        this.value = value;
        this.text = text;
    }
    /**
     * @EnumValue 用于指定映射数据库字段的值
     */
    private String value;

    private String text;

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public static GtCastTypeEm create(JSONObject json) {
        for (GtCastTypeEm gtCastTypeEm : GtCastTypeEm.values()) {
            if (gtCastTypeEm.getValue().equals(json.getInteger("value"))) {
                return gtCastTypeEm;
            }
        }
        throw new IllegalArgumentException("没有对应的"+json.getInteger("value"));
    }
}
