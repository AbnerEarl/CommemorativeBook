package com.dou361.social.share.wechat;

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
 * 创建日期：2016/6/27 22:53
 * <p>
 * 描 述：微信分享回调
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public interface IWXShareCallback {

    void onSuccess();

    void onCancel();

    void onFailure(Exception e);
}
