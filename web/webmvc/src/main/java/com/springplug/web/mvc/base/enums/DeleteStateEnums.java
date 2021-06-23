package com.springplug.web.mvc.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
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
    public static DeleteStateEnums create(String value) {
        try {
            return DeleteStateEnums.valueOf(value);
        } catch (IllegalArgumentException e) {
            for (DeleteStateEnums deleteStateEnums : DeleteStateEnums.values()) {
                if (deleteStateEnums.getValue().equals(value) || deleteStateEnums.getText().equals(value)) {
                    return deleteStateEnums;
                }
            }
            return null;
        }
    }
}
