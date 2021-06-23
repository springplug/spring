package com.springplug.web.flux.base.enums;

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

    @Getter
    private Integer value;

    @Getter
    private String text;
}
