package com.springplug.web.mvc.autoconfigure;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;

import java.util.Map;
import java.util.Objects;

@ConfigurationProperties(prefix = "spring.sys.info")
public class SysProperties {

    private static String name;

    @Value("${spring.application.name}")
    public void setName(String name) {
        SysProperties.name = name;
    }

    private String systemName;

    private String serverUrl;

    private int iscalculation=0;

    private String iconId;

    private String consoleUrl;

    private String systemUrl;

    private String systemKey;

    private String systemValue;

    private String systemOnly;

    private Map<String, String> requestInterface;

    @Autowired
    private InetUtils inetUtils;


    public String getInfo(){
        JSONObject json=new JSONObject();
        json.put("systemName", Objects.toString(systemName,name));
        json.put("serverUrl",Objects.toString(serverUrl,"http://"+this.inetUtils.findFirstNonLoopbackHostInfo().getIpAddress()));
        json.put("iscalculation",iscalculation);
        json.put("iconId",iconId);
        json.put("consoleUrl",consoleUrl);
        json.put("systemOnly",Objects.toString(systemValue,name));
        json.put("systemUrl",Objects.toString(systemUrl,json.getString("serverUrl")+"/"+json.getString("systemOnly")+"/myInterface"));
        json.put("systemKey", Objects.toString(systemKey,"zzsc_"+json.getString("systemOnly")+"_client"));
        json.put("systemValue",Objects.toString(systemValue,"zzsc_"+json.getString("systemOnly")+"_secret"));
        json.put("interface",requestInterface);
        return json.toJSONString();
    }


    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getIscalculation() {
        return iscalculation;
    }

    public void setIscalculation(int iscalculation) {
        this.iscalculation = iscalculation;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getConsoleUrl() {
        return consoleUrl;
    }

    public void setConsoleUrl(String consoleUrl) {
        this.consoleUrl = consoleUrl;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public String getSystemValue() {
        return systemValue;
    }

    public void setSystemValue(String systemValue) {
        this.systemValue = systemValue;
    }

    public String getSystemOnly() {
        return systemOnly;
    }

    public void setSystemOnly(String systemOnly) {
        this.systemOnly = systemOnly;
    }

    public Map<String, String> getRequestInterface() {
        return requestInterface;
    }

    public void setRequestInterface(Map<String, String> requestInterface) {
        this.requestInterface = requestInterface;
    }
}
