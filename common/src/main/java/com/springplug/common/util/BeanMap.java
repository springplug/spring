package com.springplug.common.util;

import com.springplug.common.util.valid.ValidUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 自定义map实现
 *
 * @param <K>
 * @param <V>
 */
public class BeanMap<K, V> extends HashMap<K, V> {


    /**
     * 无参构造
     */
    public BeanMap() {

    }

    /**
     * 有参构造
     *
     * @param map 需要转换的map
     */
    public BeanMap(Map map) {
        this.putAll(map);
    }

    /**
     * 获取List值
     *
     * @param key 查询的key
     * @return 返回值
     */
    public List getList(Object key) {
        V v = get(key);
        return getObject(() -> v instanceof List ? (ArrayList) v : null);

    }

    /**
     * 获取Map值
     *
     * @param key 查询的key
     * @return 返回值
     */
    public BeanMap getMap(Object key) {
        V v = get(key);
        return getObject(() -> v instanceof Map ? mapToBeanMap((Map) v) : null);

    }

    /**
     * 获取String值
     *
     * @param key 查询的key
     * @return 返回值
     */
    public String getString(Object key) {
        return getObject(key, k -> get(k).toString());
    }

    /**
     * 获取Double值
     *
     * @param key 查询的key
     * @return 返回值
     */
    public Double getDouble(Object key) {
        V v = get(key);
        return getObject(() -> ValidUtils.isDecmal(v.toString()) ? Util.get(v.toString(), Double::parseDouble) : (ValidUtils.isIntege(v.toString()) ? Util.get(v.toString(), Double::parseDouble) : null));
    }

    /**
     * 获取Integer值
     *
     * @param key 查询的key
     * @return 返回值
     */
    public Integer getInteger(Object key) {
        V v = get(key);
        return getObject(() -> ValidUtils.isIntege(v.toString()) ? Util.get(v.toString(), Integer::parseInt) : null);
    }


    /**
     * 获取map值
     *
     * @param key      查询的key
     * @param function
     * @return 返回值
     */
    private <T> T getObject(Object key, Function<Object, T> function) {
        return function.apply(key);
    }


    /**
     * 获取map值
     *
     * @param supplier
     * @return 返回值
     */
    private <T> T getObject(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * Map转BeanMap
     *
     * @param map 需转换的map
     * @return 转换好的BeanMap
     */
    private BeanMap mapToBeanMap(Map map) {
        if (map == null || map.isEmpty()) return new BeanMap();
        return new BeanMap(map);
    }
}
