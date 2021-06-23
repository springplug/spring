package com.springplug.data.mybatisplus.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springplug.data.mybatisplus.base.service.IBaseService;
import com.springplug.common.util.string.StringUtils;
import com.springplug.data.core.annotation.Operator;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


/**
 * @author jwt
 * @version 1.0
 * @date 2021/4/23 9:22
 */

/**
 * @param <T>  dto类
 * @param <PS> 参数VO类
 * @param <PK> 主键类型
 * @param <M>  mapper类
 */
public abstract class BaseServiceImpl<T, PS, PK extends Serializable, M extends BaseMapper<T>> extends ServiceImpl<M, T> implements IBaseService<T, PS, PK> {

    /**
     * 非等值条件的判断与添加
     *
     * @param lambdaQueryWrapper 等值查询条件
     * @param ps                 非等值查询条件
     */
    public abstract void paramsFillin(Page<T> page, LambdaQueryWrapper<T> lambdaQueryWrapper, PS ps);

    protected LambdaQueryWrapper<T> addNotEqLambdaQueryWrapper(Page<T> page, LambdaQueryWrapper<T> lambdaQueryWrapper, PS ps) {
        if (lambdaQueryWrapper == null) {
            lambdaQueryWrapper = new LambdaQueryWrapper<T>();
        }
        if (ps != null) {
            //每个属性判断：不为空则添加查询条件
            paramsFillin(page, lambdaQueryWrapper, ps);
        }
        return lambdaQueryWrapper;
    }

    @Override
    public Page<T> findAll(Page<T> page, T t, PS ps) {
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<T>(t);
        //非等值查询
        if (ps != null) {
            addNotEqLambdaQueryWrapper(page, lambdaQueryWrapper, ps);
        }
        return this.baseMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public Page<T> findAll(com.springplug.data.core.domain.Page<T> page, T t, PS ps) throws Exception{
        //非等值查询
        QueryWrapper<T> wrapper = getWrapper(t, ps);
        Page<T> tPage = new Page<>(page.getCurrent(), page.getSize());
        if (StringUtils.isNotEmpty(page.getAscs())) {
            wrapper.orderByAsc(page.getAscs().toArray(String[]::new));
        }
        if (StringUtils.isNotEmpty(page.getDescs())) {
            wrapper.orderByAsc(page.getDescs().toArray(String[]::new));
        }
        return this.baseMapper.selectPage(tPage, wrapper);
    }

    @Override
    public Page<T> findAll(Page<T> page, Wrapper<T> wrapper) {
        return this.baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<T> findAll(T t, PS ps, OrderItem... orderItems) throws Exception {
        QueryWrapper<T> wrapper = getWrapper(t, ps);
        if (StringUtils.isNotEmpty(orderItems)){
            for (OrderItem item : orderItems) {
                String column = item.getColumn();
                wrapper.orderBy(true, item.isAsc(), column);
            }
        }
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<T> findAll(T t, PS ps) {
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<>(t);
        // 非等值查询
        if (ps != null) {
            addNotEqLambdaQueryWrapper(null, lambdaQueryWrapper, ps);
        }
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<T> findAll(Wrapper<T> wrapper) {
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<T> findAll(Collection<PK> idList) {
        return this.baseMapper.selectBatchIds(idList);
    }

    @Override
    public T findOne(Map<String, Object> map) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String column = StringUtils.humpToLine(entry.getKey());
            wrapper.eq(column, entry.getValue());
        }
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public T findOne(String column, Object value) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        column = StringUtils.humpToLine(column);
        wrapper.eq(column, value);
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 如果逻辑删除请重写此方法
     *
     * @param t  等值查询条件
     * @param ps 非等值查询条件
     * @return
     */
    @Deprecated
    @Override
    public Integer deleteWsLogic(T t, PS ps) {
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<>(t);
        //非等值查询
        if (ps != null) {
            addNotEqLambdaQueryWrapper(null, lambdaQueryWrapper, ps);
        }
        return this.baseMapper.delete(lambdaQueryWrapper);
    }

    private QueryWrapper<T> getWrapper(T t, PS ps) throws Exception{
        QueryWrapper<T> wrapper = new QueryWrapper<>(t);
        Class<?> clazz = ps.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            List<Annotation> annotationList = Arrays.asList(annotations);
            Object o = field.get(ps);
            if (null != o) {
                for (Annotation annotation : annotationList) {
                    if(annotation instanceof Operator){
                        Operator operator = (Operator) annotation;
                        String column = operator.column();
                        if (StringUtils.isEmpty(column)) {
                            column = field.getName();
                        }
                        column = StringUtils.humpToLine(column);
                        switch (operator.value()){
                            case EQ:
                                wrapper.eq(column, o);
                                break;
                            case LIKE:
                                wrapper.like(column, o);
                                break;
                            case LIKELEFT:
                                wrapper.likeLeft(column, o);
                                break;
                            case LIKERIGHT:
                                wrapper.likeRight(column, o);
                                break;
                            case GT:
                                wrapper.gt(column, o);
                                break;
                            case GTE:
                                wrapper.ge(column, o);
                                break;
                            case LT:
                                wrapper.lt(column, o);
                                break;
                            case LTE:
                                wrapper.le(column, o);
                                break;
                        }
                    }
                }
            }
        }
        return wrapper;
    }

}