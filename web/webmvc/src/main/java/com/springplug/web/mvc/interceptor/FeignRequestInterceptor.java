package com.springplug.web.mvc.interceptor;

import com.springplug.common.util.string.StringUtils;
import com.springplug.common.util.json.JsonUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @effect: feign调用进行数据转化
 * @author jwt
 * @version 1.0
 * @date 2021-4-27 10:26
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {
        if (null != template.queries()&&template.queries().size()>0) {
            try {
                //原始查询
                Map<String, Collection<String>> OriginalQueries = template.queries();

                //自定义查询
                Map<String, Collection<String>> queries = new HashMap<>();

                OriginalQueries.forEach((k,v)->{
                    if(StringUtils.isNotEmpty(v) &&v.size()==1){
                        String json=StringUtils.getURLDecoderString(v.toArray()[0].toString());
                        Map<String,Object> map = JsonUtil.toMap(json);
                        if(null==map||map.size()==0){
                            queries.put(k,v);
                        }else{
                            map.forEach((k1,v1)->{
                                if(null!=v1){
                                    Collection<String> strings = new ArrayList<>(1);
                                    strings.add(v1.toString());
                                    queries.put(k1,strings);
                                }
                            });
                        }
                    }else{
                        queries.put(k,v);
                    }
                });

                template.queries(null);
                template.queries(queries);
            } catch (Exception e) {
                throw new RuntimeException("json转化异常");
            }
        }
    }
}

