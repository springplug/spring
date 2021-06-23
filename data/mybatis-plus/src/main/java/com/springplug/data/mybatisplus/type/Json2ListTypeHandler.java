package com.springplug.data.mybatisplus.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.io.Serializable;
import java.util.List;

public class Json2ListTypeHandler<P extends Serializable> extends AbstractJsonTypeHandler<List<P>> {

    @Override
    protected List<P> parse(String json) {
        List<P> res = JSON.parseObject(json, new TypeReference<List<P>>(){});
        return res;
    }

    @Override
    protected String toJson(List<P> obj) {
        return JSON.toJSONString(obj, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty});
    }
}
