package com.dou361.social.otto;

import com.dou361.social.model.SocialToken;
import com.dou361.social.model.SocialUser;

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
 * 创建日期：2016/6/27 22:52
 * <p>
 * 描 述：登录事件对象
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SSOBusEvent {

    public static final int PLATFORM_DEFAULT = 0;
    public static final int PLATFORM_WEIBO = 1;
    public static final int PLATFORM_WECHAT = 2;
    public static final int PLATFORM_QQ = 3;

    public static final int TYPE_GET_TOKEN = 0;
    public static final int TYPE_GET_USER = 1;
    public static final int TYPE_FAILURE = 2;
    public static final int TYPE_CANCEL = 3;

    private int type;
    private int platform;
    private SocialUser user;
    private SocialToken token;
    private Exception exception;

    public SSOBusEvent(int type, int platform) {
        this.type = type;
        this.platform = platform;
    }

    public SSOBusEvent(int type, int platform, SocialUser user) {
        this.type = type;
        this.platform = platform;
        this.user = user;
    }

    public SSOBusEvent(int type, int platform, SocialToken token) {
        this.type = type;
        this.platform = platform;
        this.token = token;
    }

    public SSOBusEvent(int type, int platform, Exception exception) {
        this.type = type;
        this.platform = platform;
        this.exception = exception;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SocialUser getUser() {
        return user;
    }

    public void setUser(SocialUser user) {
        this.user = user;
    }

    public SocialToken getToken() {
        return token;
    }

    public void setToken(SocialToken token) {
        this.token = token;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
