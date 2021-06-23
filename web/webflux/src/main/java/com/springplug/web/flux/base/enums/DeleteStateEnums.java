package com.springplug.web.flux.base.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springplug.data.core.enums.BaseEnum;
import lombok.Getter;

/**
 * 是否删除状态 0否 1是
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeleteStateEnums implements BaseEnum {

    NOT_DELETE(0, "未删除"),DELETE(1, "已删除");

    DeleteStateEnums(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @Getter
    private Integer value;

    @Getter
    private String text;
}
