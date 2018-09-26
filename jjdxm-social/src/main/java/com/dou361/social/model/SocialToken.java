package com.dou361.social.model;

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
 * 创建日期：2016/6/27 22:44
 * <p>
 * 描 述：oauth token信息
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialToken {

    private String openId;
    private String token;
    private String refreshToken;
    private String result;
    private long expiresTime;

    public SocialToken() {
    }

    public SocialToken(String openId, String token, String refreshToken, long expiresTime) {
        this.openId = openId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresTime = System.currentTimeMillis() + expiresTime * 1000L;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SocialToken# openId=" + openId + ", token=" + token + ", refreshToken=" + refreshToken + ", expiresTime=" + expiresTime;
    }
}
