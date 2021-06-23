package com.springplug.web.mvc.metaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.springplug.web.mvc.util.ServletUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyIntegralMetaObjectHandler implements MetaObjectHandler {

    /**
     * 执行添加操作时，提供默认值
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置默认创建时间
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime", new Date(), metaObject);
        }
        // 设置默认操作人
        Object createBy = getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            String id = ServletUtils.getHeader("Authorization-id");
            setFieldValByName("createBy", id, metaObject);
        }

    }

    /**
     * 执行修改操作时，提供默认值
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //设置默认更新时间
        Object lsUpdateTime = getFieldValByName("lsUpdateTime", metaObject);
        if (lsUpdateTime == null){
            setFieldValByName("lsUpdateTime",new Date(),metaObject);
        }
        // 设置默认修改操作人
        Object updateBy = getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            String id = ServletUtils.getHeader("Authorization-id");
            setFieldValByName("updateBy", id, metaObject);
        }

    }
}
