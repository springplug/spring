package com.springplug.push.gt.client.cofig;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import com.springplug.push.gt.client.Mapper.PushResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/11 18:57
 */
@Component
@RefreshScope
public class GtConfig {

    @Autowired
    private PushResultMapper pushResultMapper;

    @Value("#{${gtConfig}}")
    List<Map<String, String>> gtAppList;

    @Value(("#{${gtPackage}}"))
    Map<String, String> gtPackage;

    static Map<String, PushApi> pushMap = new ConcurrentHashMap<>();
    static Map<String, UserApi> userMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init(){
        // 初始化推送记录表
        pushResultMapper.initTable();

        // 初始化个推项目api
        for (Map<String, String> stringMap : gtAppList) {
            initPushApi(stringMap);
        }
    }

    private void initPushApi(Map<String, String> map) {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        apiConfiguration.setAppId(map.get("appId"));
        apiConfiguration.setAppKey(map.get("appKey"));
        apiConfiguration.setMasterSecret(map.get("masterSecret"));
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        apiConfiguration.setAnalyseStableDomainInterval(500);
        apiConfiguration.setOpenCheckHealthDataSwitch(true);
        apiConfiguration.setCheckHealthInterval(500);
        apiConfiguration.setOpenAnalyseStableDomainSwitch(false);  //关闭
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        UserApi userApi = apiHelper.creatApi(UserApi.class);
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        userMap.put(map.get("name"), userApi);
        pushMap.put(map.get("name"), pushApi);
    }

    // 获取推送api
    public PushApi getPushBeanName(String name){
        return pushMap.get(name);
    }

    // 获取用户api
    public UserApi getUserBeanName(String name){
        return userMap.get(name);
    }

    // 获取包名
    public String getPackageName(String name) {
        return gtPackage.get(name);
    }

}
