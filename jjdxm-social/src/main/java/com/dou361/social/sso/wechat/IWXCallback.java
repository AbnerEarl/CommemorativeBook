package com.dou361.social.sso.wechat;

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
 * 创建日期：2016/6/27 22:55
 * <p>
 * 描 述：微信授权回调接口
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public interface IWXCallback {
    void onGetCodeSuccess(String code);

    void onGetTokenSuccess(SocialToken token);

    void onGetUserInfoSuccess(SocialUser user);

    void onFailure(Exception e);

    void onCancel();
}
