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
 * 创建日期：2016/6/27 22:44
 * <p>
 * 描 述：社会化分享数据类
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialShareScene implements Serializable {

    public static final int SHARE_TYPE_DEFAULT = 0;
    public static final int SHARE_TYPE_WEIBO = 1;
    public static final int SHARE_TYPE_WECHAT = 2;
    public static final int SHARE_TYPE_WECHAT_TIMELINE = 3;
    public static final int SHARE_TYPE_QQ = 4;
    public static final int SHARE_TYPE_QZONE = 5;

    private int id;
    private int type;
    private String appName;
    private String title;
    private String desc;
    private String thumbnail;
    private String url;

    /**
     * @param id        分享唯一标识符，可随意指定，会在分享结果ShareBusEvent中返回
     * @param appName   分享到QQ时需要指定，会在分享弹窗中显示该字段
     * @param type      分享类型，会在分享结果ShareBusEvent中作为platform返回
     * @param title     标题
     * @param desc      简短描述
     * @param thumbnail 缩略图网址
     * @param url       WEB网址
     */
    public SocialShareScene(int id, String appName, int type, String title, String desc, String thumbnail, String url) {
        this.id = id;
        this.appName = appName;
        this.type = type;
        this.title = title;
        this.desc = desc;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public SocialShareScene(int id, String appName, String title, String desc, String thumbnail, String url) {
        this.id = id;
        this.appName = appName;
        this.title = title;
        this.desc = desc;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        if (title != null && title.length() > 31) {
            return title.substring(0, 30);
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        if (desc != null && desc.length() > 31) {
            return desc.substring(0, 30);
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
