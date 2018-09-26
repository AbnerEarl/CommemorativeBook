package com.dou361.social.model;

import java.io.Serializable;
/**
 * ========================================
 * <p>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p>
 * 作 者：陈冠明
 * <p>
 * 个人网站：http://www.dou361.com
 * <p>
 * 版 本：1.0
 * <p>
 * 创建日期：2016/6/27 22:38
 * <p>
 * 描 述：社交平台信息
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialInfo implements Serializable {

    private boolean debugMode = false;
    private String wechatAppId = "";
    private String weChatAppSecret = "";
    private String weChatScope = "snsapi_userinfo";
    private String weiboAppKey = "";
    private String weiboRedirectrUrl = "http://www.sina.com";
    private String weiboScope = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private String qqAppId = "";
    private String qqScope = "all";

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getWechatAppId() {
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWeChatAppSecret() {
        return weChatAppSecret;
    }

    public void setWeChatAppSecret(String weChatAppSecret) {
        this.weChatAppSecret = weChatAppSecret;
    }

    public String getWeChatScope() {
        return weChatScope;
    }

    public void setWeChatScope(String weChatScope) {
        this.weChatScope = weChatScope;
    }

    public String getWeiboAppKey() {
        return weiboAppKey;
    }

    public void setWeiboAppKey(String weiboAppKey) {
        this.weiboAppKey = weiboAppKey;
    }

    public String getWeiboRedirectrUrl() {
        return weiboRedirectrUrl;
    }

    public void setWeiboRedirectrUrl(String weiboRedirectrUrl) {
        this.weiboRedirectrUrl = weiboRedirectrUrl;
    }

    public String getWeiboScope() {
        return weiboScope;
    }

    public void setWeiboScope(String weiboScope) {
        this.weiboScope = weiboScope;
    }

    public String getQqAppId() {
        return qqAppId;
    }

    public void setQqAppId(String qqAppId) {
        this.qqAppId = qqAppId;
    }

    public String getQqScope() {
        return qqScope;
    }

    public void setQqScope(String qqScope) {
        this.qqScope = qqScope;
    }

    public String getUrlForWeChatToken() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + getWechatAppId()
                + "&secret="
                + getWeChatAppSecret()
                + "&code=%s&grant_type=authorization_code";
    }

    public String getUrlForWeChatUserInfo() {
        return "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    }

    public String getUrlForWeChatRefreshToken() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                + getWechatAppId()
                + "&grant_type=refresh_token&refresh_token=%s";
    }
}
