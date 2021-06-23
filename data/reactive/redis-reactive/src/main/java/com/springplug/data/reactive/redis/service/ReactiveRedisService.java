package com.springplug.data.reactive.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.*;

@Component
public class ReactiveRedisService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */

    public Mono<Boolean> expire(String key, long time) {
        try {
            if (time > 0) {
                return redisTemplate.expire(key, Duration.ofSeconds(time));
            }
            return result(true);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */

    public Mono<Long> getExpire(String key) {
        return redisTemplate.getExpire(key).flatMap(d->Mono.just(d.getSeconds()));
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */

    public Mono<Boolean> hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);

        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */

    public Mono<Long> del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return redisTemplate.delete(key[0]);
            } else {
                return redisTemplate.delete(key);
            }
        }
        return result(0L);
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */

    public Mono<Object> get(String key) {
        return key == null ? Mono.empty() : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */

    public Mono<Boolean> set(String key, Object value) {
        try {
            return redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */

    public Mono<Boolean> set(String key, Object value, long time) {
        try {
            if (time > 0) {
                return redisTemplate.opsForValue().set(key, value,Duration.ofSeconds(time));
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */

    public Mono<Long> incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);

    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */

    public Mono<Long> decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);

    }

    // ================================Map=================================


    /**
     * getByKeyAndHashKey
     *
     * @param key  键 不能为null
     * @param hashKey 键 不能为null
     * @return 值
     */
    public Mono<Object> getByKeyAndHashKey(String key, Object hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Mono<Object> hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);

    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */

    public Flux<Map.Entry<Object, Object>> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);

    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */

    public Mono<Boolean> hmset(String key, Map<String, Object> map) {
        try {
            return redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */

    public Mono<Boolean> hmset(String key, Map<String, Object> map, long time) {
        try {
            return redisTemplate.opsForHash().putAll(key, map).flatMap(b->{
                if(b){
                    if(time>0){
                        expire(key,time);
                    }
                    return result(true);
                }
                return result(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */

    public Mono<Boolean> hset(String key, String item, Object value) {
        try {
            return redisTemplate.opsForHash().put(key, item, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */

    public Mono<Boolean> hset(String key, String item, Object value, long time) {
        try {
            return redisTemplate.opsForHash().put(key, item, value).flatMap(b->{
                if(b){
                    if (time > 0) {
                        expire(key, time);
                    }
                    return result(true);
                }
                return result(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     */

    public Mono<Long> hdel(String key, Object... item) {
        return redisTemplate.opsForHash().remove(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */

    public Mono<Boolean> hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */

    public Mono<Double> hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */

    public Mono<Double> hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */

    public Flux<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */

    public Mono<Boolean> sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public Mono<Long> sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public Mono<Long> sSetAndTime(String key, long time, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values).flatMap(l->{
                if(l>0){
                    if (time > 0) {
                        expire(key, time).subscribe();
                    }
                    return result(l);
                }
                return result(0L);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */

    public Mono<Long> sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */

    public Mono<Long> setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */

    public Flux<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */

    public Mono<Long> lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */

    public Mono<Object> lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */

    public Mono<Long> lSet(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */

    public Mono<Long> lSet(String key, Object value, long time) {
        try {
            return redisTemplate.opsForList().rightPush(key, value).flatMap(l->{
                if(l>0){
                    if (time > 0) {
                        expire(key, time);
                        return result(1L);
                    }
                    return result(1L);
                }
                return result(0L);
            });

        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */

    public Mono<Long> lSet(String key, List<Object> value) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */

    public Mono<Long> lSet(String key, List<Object> value, long time) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, value).flatMap(l->{
                if(l>0){
                    if (time > 0) {
                        expire(key, time);
                    }
                    return result(l);
                }
                return result(0L);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */

    public Mono<Boolean> lUpdateIndex(String key, long index, Object value) {
        try {
            return redisTemplate.opsForList().set(key, index, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(false);
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */

    public Mono<Long> lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return result(0L);
        }
    }

    public Mono<Boolean> result(Boolean b){
        return Mono.just(b);
    }

    public Mono<Long> result(Long l){
        return Mono.just(l);
    }
}
