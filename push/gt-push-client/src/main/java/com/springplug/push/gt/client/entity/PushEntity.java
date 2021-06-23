package com.springplug.push.gt.client.entity;

import com.alibaba.fastjson.JSONObject;
import com.springplug.common.util.string.StringUtils;
import com.springplug.push.gt.client.enums.GtCastTypeEm;
import com.springplug.push.gt.client.enums.GtClickTypeEm;
import com.springplug.push.gt.client.enums.GtTypeEm;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/12 13:40
 */
public class PushEntity {
    private List<String> cidList; // 推送目标用户，单推只能填一个
    private GtTypeEm gtType; // 通知 || 消息
    private GtCastTypeEm castTypeEm; // 推送方式
    private String title; // 通知标题
    private String body; // 通知内容
    private String bigText; // 长文本消息内容，通知消息+长文本样式,与bigImage二选一,两个都填写时报错
    private String bigImage; // 大图的URL地址，通知消息+大图样式,与bigText二选一,两个都填写时报错
    private String logo; // 通知图标名称,含后缀名，需客户但开发时嵌入
    private String logoUrl; // 通知图标URL地址，长度 ≤ 256
    private GtClickTypeEm gtClickTypeEm; // 点击后续动作
    private String url; // 后续动作的参数(可以是url，也可以是activity，或是自定义消息)
    private JSONObject param; // 打开应用内页面所需要的参数
    private List<JSONObject> condition; //  参数;条件推 传
    private String tag; // 标签推传
    private List<Long> userIdList; // 用户id
    private String appClass; // 项目唯一标识
    private Long scheduleTime; // 定时发送时间,该功能需开通vip
    private String messageType; // 发送类型

    // 添加cid
    public void addCid(String cid){
        if (StringUtils.isEmpty(cidList)) {
            cidList = new ArrayList<>();
        }
        cidList.add(cid);
    }

    // 添加cid
    public void addUserId(Long userId){
        if (StringUtils.isEmpty(userIdList)) {
            userIdList = new ArrayList<>();
        }
        userIdList.add(userId);
    }

    public List<String> getCidList() {
        return cidList;
    }

    public void setCidList(List<String> cidList) {
        this.cidList = cidList;
    }

    public GtTypeEm getGtType() {
        return gtType;
    }

    public void setGtType(GtTypeEm gtType) {
        this.gtType = gtType;
    }

    public GtCastTypeEm getCastTypeEm() {
        return castTypeEm;
    }

    public void setCastTypeEm(GtCastTypeEm castTypeEm) {
        this.castTypeEm = castTypeEm;
    }

    public void setGtClickTypeEm(GtClickTypeEm gtClickTypeEm) {
        this.gtClickTypeEm = gtClickTypeEm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public GtClickTypeEm getGtClickTypeEm() {
        return gtClickTypeEm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }

    public List<JSONObject> getCondition() {
        return condition;
    }

    public void setCondition(List<JSONObject> condition) {
        this.condition = condition;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public String getAppClass() {
        return appClass;
    }

    public void setAppClass(String appClass) {
        this.appClass = appClass;
    }

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public static PushEntity.Builder builder() {
        return new PushEntity.Builder();
    }

    public static class Builder {
        private List<String> cidList; // 推送目标用户，单推只能填一个
        private GtTypeEm gtType; // 通知 || 消息
        private GtCastTypeEm castTypeEm; // 推送方式
        private String title; // 通知标题
        private String body; // 通知内容
        private String bigText; // 长文本消息内容，通知消息+长文本样式,与bigImage二选一,两个都填写时报错
        private String bigImage; // 大图的URL地址，通知消息+大图样式,与bigText二选一,两个都填写时报错
        private String logo; // 通知图标名称,含后缀名，需客户但开发时嵌入
        private String logoUrl; // 通知图标URL地址，长度 ≤ 256
        private GtClickTypeEm gtClickTypeEm; // 点击后续动作
        private String url; // 后续动作的参数(可以是url，也可以是activity，或是自定义消息)
        private JSONObject param; // 打开应用内页面所需要的参数
        private List<JSONObject> condition; //  参数  ;条件推 传
        private String tag; // 标签推传
        private List<Long> userIdList; // 用户id
        private String appClass; // 项目唯一标识
        private Long scheduleTime; // 定时发送时间,该功能需开通vip
        private String messageType; // 发送类型

        Builder() {
        }


        public PushEntity.Builder cidList(List<String> cidList) {
            this.cidList = cidList;
            return this;
        }

        public PushEntity.Builder gtType(GtTypeEm gtType) {
            this.gtType = gtType;
            return this;
        }

        public PushEntity.Builder castTypeEm(GtCastTypeEm castTypeEm) {
            this.castTypeEm = castTypeEm;
            return this;
        }

        public PushEntity.Builder title(String title) {
            this.title = title;
            return this;
        }

        public PushEntity.Builder body(String body) {
            this.body = body;
            return this;
        }

        public PushEntity.Builder bigText(String bigText) {
            this.bigText = bigText;
            return this;
        }

        public PushEntity.Builder bigImage(String bigImage) {
            this.bigImage = bigImage;
            return this;
        }

        public PushEntity.Builder logo(String logo) {
            this.logo = logo;
            return this;
        }

        public PushEntity.Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public PushEntity.Builder gtClickTypeEm(GtClickTypeEm gtClickTypeEm) {
            this.gtClickTypeEm = gtClickTypeEm;
            return this;
        }

        public PushEntity.Builder url(String url) {
            this.url = url;
            return this;
        }

        public PushEntity.Builder param(JSONObject param) {
            this.param = param;
            return this;
        }

        public PushEntity.Builder condition(List<JSONObject> condition) {
            this.condition = condition;
            return this;
        }

        public PushEntity.Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public PushEntity.Builder userIdList(List<Long> userIdList) {
            this.userIdList = userIdList;
            return this;
        }

        public PushEntity.Builder appClass(String appClass) {
            this.appClass = appClass;
            return this;
        }

        public PushEntity.Builder scheduleTime(Long scheduleTime) {
            this.scheduleTime = scheduleTime;
            return this;
        }

        public PushEntity.Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public PushEntity build() {
            Assert.notNull(gtType, "gtType must not null");
            Assert.notNull(castTypeEm, "castTypeEm must not null");
            Assert.notNull(title, "title must not null");
            Assert.notNull(body, "body must not null");
            Assert.notNull(gtClickTypeEm, "gtClickTypeEm must not null");
            Assert.notNull(appClass, "appClass must not null");
            Assert.isTrue(this.castTypeEm == GtCastTypeEm.ALL_CAST || !StringUtils.isEmpty(this.cidList), "cidList must not null in not all pushing");
            return new PushEntity(cidList, gtType, castTypeEm, title, body, bigText, bigImage, logo, logoUrl, gtClickTypeEm, url, param, condition, tag, userIdList, appClass, scheduleTime, messageType);
        }

    }

    public PushEntity(List<String> cidList, GtTypeEm gtType, GtCastTypeEm castTypeEm, String title, String body, String bigText, String bigImage, String logo, String logoUrl, GtClickTypeEm gtClickTypeEm, String url, JSONObject param, List<JSONObject> condition, String tag, List<Long> userIdList, String appClass, Long scheduleTime, String messageType) {
        this.cidList = cidList;
        this.gtType = gtType;
        this.castTypeEm = castTypeEm;
        this.title = title;
        this.body = body;
        this.bigText = bigText;
        this.bigImage = bigImage;
        this.logo = logo;
        this.logoUrl = logoUrl;
        this.gtClickTypeEm = gtClickTypeEm;
        this.url = url;
        this.param = param;
        this.condition = condition;
        this.tag = tag;
        this.userIdList = userIdList;
        this.appClass = appClass;
        this.scheduleTime = scheduleTime;
        this.messageType = messageType;
    }

    public PushEntity(GtTypeEm gtType, GtCastTypeEm castTypeEm, String title, String body, GtClickTypeEm gtClickTypeEm, String appClass) {
        this.gtType = gtType;
        this.castTypeEm = castTypeEm;
        this.title = title;
        this.body = body;
        this.gtClickTypeEm = gtClickTypeEm;
        this.appClass = appClass;
    }

    private PushEntity(){}

    //    // 添加and条件
//    public void addAndCon(GtConditionEm conditionEm, String... values){
//        GtConditionEntity entity = new GtConditionEntity(conditionEm.getValue(), GtOptTypeEm.TYPE_AND, values);
//        if (StringUtils.isEmpty(condition)) {
//            condition = new ArrayList<>();
//        }
//        condition.add(entity);
//    }
//
//    // 添加not条件
//    public void addNotCon(GtConditionEm conditionEm, String... values){
//        GtConditionEntity entity = new GtConditionEntity(conditionEm.getValue(), GtOptTypeEm.TYPE_NOT, values);
//        if (StringUtils.isEmpty(condition)) {
//            condition = new ArrayList<>();
//        }
//        condition.add(entity);
//    }
//
//    // 添加or条件
//    public void addOrCon(GtConditionEm conditionEm, String... values){
//        GtConditionEntity entity = new GtConditionEntity(conditionEm.getValue(), GtOptTypeEm.TYPE_OR, values);
//        if (StringUtils.isEmpty(condition)) {
//            condition = new ArrayList<>();
//        }
//        condition.add(entity);
//    }
}
