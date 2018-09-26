package com.dou361.social.otto;

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
 * 创建日期：2016/6/27 22:40
 * <p>
 * 描 述：分享事件对象
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class ShareBusEvent {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILURE = 1;
    public static final int TYPE_CANCEL = 2;

    private int type;
    private int id;
    private int platform;
    private Exception exception;

    public ShareBusEvent(int type, int platform) {
        this.type = type;
        this.platform = platform;
    }

    public ShareBusEvent(int type, int platform, int id) {
        this.type = type;
        this.platform = platform;
        this.id = id;
    }

    public ShareBusEvent(int type, int platform, Exception exception) {
        this.type = type;
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
