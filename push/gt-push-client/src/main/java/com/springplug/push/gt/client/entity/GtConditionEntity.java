package com.springplug.push.gt.client.entity;

import com.springplug.push.gt.client.enums.GtOptTypeEm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * gt条件推送对象
 * @author jwt
 * @version 1.0
 * @date 2021/3/12 16:03
 */
public class GtConditionEntity {

    private String key;
    private Set<String> value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Set<String> value) {
        this.value = value;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    /**
     * 条件关联方式
     *
     * @see
     */
    private String optType;

    public GtConditionEntity(String key, GtOptTypeEm typeEm, String... values){
        this.key = key;
        this.value = new HashSet<>(Arrays.asList(values));
        this.optType = typeEm.getValue();
    }

    public GtConditionEntity(String key, Set<String> values, GtOptTypeEm typeEm){
        this.key = key;
        this.value = values;
        this.optType = typeEm.getValue();
    }

    public GtConditionEntity(String key, Set<String> value, String optType) {
        this.key = key;
        this.value = value;
        this.optType = optType;
    }
}
