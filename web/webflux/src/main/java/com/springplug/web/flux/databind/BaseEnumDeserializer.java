package com.springplug.web.flux.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.springplug.common.util.json.JsonUtil;
import com.springplug.data.core.enums.BaseEnum;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.util.Map;

public class BaseEnumDeserializer extends JsonDeserializer<Enum> {

    @SneakyThrows
    @Override
    public Enum deserialize(JsonParser jp, DeserializationContext ctxt){
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        if(BaseEnum.class.isAssignableFrom(findPropertyType)){
            return create(findPropertyType,node.textValue());
        }
        return Enum.valueOf(findPropertyType,node.textValue());
    }

    public <T extends BaseEnum> Enum create(Class<T> findPropertyType, String value){

        Map<String, Object> stringObjectMap = JsonUtil.toMap(value);
        if(null!=stringObjectMap){
            Object obj = stringObjectMap.get("value");
            if(null==obj){
                return null;
            }
            value=stringObjectMap.get("value").toString();
        }
        for (T enumObj : findPropertyType.getEnumConstants()) {
            if (value.equals(String.valueOf(enumObj.getValue()))||value.equals(enumObj.getText())) {
                return (Enum)enumObj;
            }
        }
        return null;
    }
}
