package com.springplug.data.r2dbc.converter;


import com.springplug.data.core.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import java.io.Serializable;

@WritingConverter
public class EnumWriteConverter implements Converter<BaseEnum, Serializable> {

    @Override
    public Serializable convert(BaseEnum baseEnum) {
        return baseEnum.getValue();
    }
}
