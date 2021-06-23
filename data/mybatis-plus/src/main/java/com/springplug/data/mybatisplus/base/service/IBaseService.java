package com.springplug.data.mybatisplus.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/4/23 9:21
 */
public interface IBaseService<T,PS,PK extends Serializable> extends IService<T> {

    /**
     * 根据传入值返回分页信息（不走注解）
     * @param page 分页信息
     * @param t 等值查询条件
     * @param ps 非等值查询条件
     * @return
     */
    Page<T> findAll(Page<T> page, T t, PS ps);

    /**
     * 根据传入值返回分页信息（不走注解）
     * @param page 分页信息
     * @param t 等值查询条件
     * @param ps 非等值查询条件
     * @return
     * @throws Exception
     */
    Page<T> findAll(com.springplug.data.core.domain.Page<T> page, T t, PS ps) throws Exception;

    /**
     * 根据传入值返回分页信息
     * @param page 分页信息
     * @param wrapper 查询条件
     * @return
     */
    Page<T> findAll(Page<T> page, Wrapper<T> wrapper);

    /**
     * 根据传入值返回所有信息(走注解)
     * @param t 等值查询条件
     * @param ps 非等值查询条件
     * @param orderItems 排序字段，可为空
     * @return
     * @throws Exception
     */
    List<T> findAll(T t, PS ps, OrderItem... orderItems) throws Exception;

    /**
     * 根据传入值返回所有信息（不走注解）
     * @param t 等值查询条件
     * @param ps 非等值查询条件
     * @return
     */
    List<T> findAll(T t, PS ps);

    /**
     * 根据传入值返回所有信息
     * @param wrapper 查询条件
     * @return
     */
    List<T> findAll(Wrapper<T> wrapper);

    /**
     * 通过主键id集合查找数据
     * @param idList 主键集合
     * @return
     */
    List<T> findAll(Collection<PK> idList);

    /**
     * 通过条件查找单条数据
     * @param map 条件
     * @return
     */
    T findOne(Map<String, Object> map);

    /**
     * 通过条件查找单条数据
     * @param column
     * @param value
     * @return
     */
    T findOne(String column, Object value);

    /**
     * 批量删除（条件删除）
     * 批量删除（逻辑删除必须重写）
     * @param t 等值查询条件
     * @param ps 非等值查询条件
     * @return
     */
    Integer deleteWsLogic(T t, PS ps);
}
