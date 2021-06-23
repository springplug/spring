package com.springplug.web.mvc.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.springplug.data.core.enums.BaseEnum;
import lombok.Getter;

/**
 * 是否启用状态 0否 1是
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnableStateEnums implements BaseEnum {

    DISABLE(0, "禁用"),ENABLE(1, "启用");

    EnableStateEnums(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @EnumValue
    @Getter
    private Integer value;

    @Getter
    private String text;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static EnableStateEnums create(String value) {
        try {
            return EnableStateEnums.valueOf(value);
        } catch (IllegalArgumentException e) {
            for (EnableStateEnums enableStateEnums : EnableStateEnums.values()) {
                if (enableStateEnums.getValue().equals(value) || enableStateEnums.getText().equals(value)) {
                    return enableStateEnums;
                }
            }
            return null;
        }
    }
}
