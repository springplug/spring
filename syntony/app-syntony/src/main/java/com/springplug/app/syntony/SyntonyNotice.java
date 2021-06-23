package com.springplug.app.syntony;


import com.alibaba.fastjson.JSONObject;
import com.springplug.common.util.string.StringUtils;
import com.springplug.common.util.json.JsonUtil;
import com.springplug.data.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-22 17:14
 */
@Slf4j
@Component
public class SyntonyNotice {

    @Autowired
    @LoadBalanced
    private RestTemplate loadBalanced;
    @Autowired
    private RedisService redisService;
    @Value("${zzuser.sys.redis}")
    private String sysKey;

    /**
     * 查询子系统列表并广播
     *
     * @param interType
     * @param t
     * @return
     */
    public <T> Map<String, Object> execSubsysList(String interType, T t) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", t);
        result.put("code", 200);
        //过滤对应接口并循环发送
        Optional.ofNullable(getSysList()).ifPresent(sys -> sys.stream().filter(s -> null != s.get("subSystemInterface") && StringUtils.isNotEmpty(s.get("subSystemInterface").toString())).map(a -> JSONObject.parseObject(a.get("subSystemInterface").toString())).map(s -> s.get(interType)).forEach(interfaceUrl -> {

            //发送http请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(JsonUtil.toJson(t), headers);
            ResponseEntity<JSONObject> post = loadBalanced.exchange(interfaceUrl.toString(), HttpMethod.POST, requestEntity, JSONObject.class);

            JSONObject body = post.getBody();

            //错误记录
            if (StringUtils.isNull(body.get("code"))) {
                log.error("发送请求失败：" + post);
                result.put("code", 500);
                result.put("msg", "服务器异常");
                return;
            }
            if ("500".equals(body.get("code").toString())) {
                log.error(interType + "：调用失败。");
                result.put("code", post.getBody().get("code"));
                result.put("msg", post.getBody().get("msg"));
                return;
            }

            Object data = post.getBody().get("data");
            if (StringUtils.isNotNull(data)) {
                if (data instanceof List) {
                    List<?> resultList = new ArrayList<>();
                    if (StringUtils.isNotEmpty((List) data)) {
                        resultList = JsonUtil.toList(JsonUtil.toJson(data), ((List) t).get(0).getClass());
                    }
                    result.put("data", resultList);
                } else if (data instanceof Set) {
                    Set<?> resultSet = new HashSet<>();
                    if (((HashSet) data).size() > 0) {
                        resultSet = new HashSet<>(JsonUtil.toList(JsonUtil.toJson(data), ((HashSet) t).iterator().next().getClass()));
                    }
                    result.put("data", resultSet);
                } else if (data instanceof Map) {
                    result.put("data", JsonUtil.toBean(JsonUtil.toJson(data), t.getClass()));
                }
            }
        }));
        return result;
    }

    /**
     * 查询子系统列表
     *
     * @return
     */
    private List<Map<String, Object>> getSysList() {
        Object o = redisService.get(sysKey);
        List<Map<String, Object>> list = JsonUtil.toListMap(o.toString());
        list.sort((o1, o2) -> Integer.parseInt(o2.get("weight").toString()) - Integer.parseInt(o1.get("weight").toString()));
        return list;
    }


}
