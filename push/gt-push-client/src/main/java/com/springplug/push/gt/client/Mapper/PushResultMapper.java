package com.springplug.push.gt.client.Mapper;

import com.springplug.push.gt.client.entity.PushResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-11 16:23
 */
@Mapper
public interface PushResultMapper {

    @Insert("CREATE TABLE IF NOT EXISTS `push_result` (\n" +
            "  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增主键',\n" +
            "  `action` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '执行方法',\n" +
            "  `parameter` longtext COLLATE utf8mb4_unicode_ci COMMENT '参数',\n" +
            "  `result` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回结果',\n" +
            "  `result_code` int(1) DEFAULT NULL COMMENT '执行结果 0成功 -1失败',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  PRIMARY KEY (`id`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=60340 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;")
    int initTable();

    @Insert("insert into push_result(action,parameter,result,result_code,create_time)values(#{action},#{parameter},#{result},#{resultCode},#{createTime})")
    int addPushResult(PushResult pushResult);
}
