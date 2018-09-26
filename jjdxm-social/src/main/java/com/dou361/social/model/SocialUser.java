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
 * 描 述：oauth用户信息
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialUser {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_WEIBO = 1;
    public static final int TYPE_WECHAT = 2;
    public static final int TYPE_QQ = 3;

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private int type;
    private String name;
    private String avatar;
    private int gender;
    private String desc;
    private String result;
    private SocialToken token;

    public SocialUser() {
    }

    public SocialUser(int type, String name, String avatar, int gender, SocialToken token) {
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.token = token;
    }

    public SocialUser(int type, String name, String avatar, int gender, String desc, SocialToken token) {
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.desc = desc;
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SocialToken getToken() {
        return token;
    }

    public void setToken(SocialToken token) {
        this.token = token;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isTokenValid() {
        return getToken().getToken() != null && System.currentTimeMillis() < getToken().getExpiresTime();
    }

    @Override
    public String toString() {
        return "SocialUser: type=" + type + ", name=" + name + ", avatar=" + avatar + ", gender=" + gender + ", desc=" + desc
                + ", token=" + token.getToken();
    }
}
