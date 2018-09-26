package com.dou361.social;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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
 * 描 述：之所以抽取WeChat类，是因为IWXAPI需要在SSO授权和分享同时用到
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class WeChat {

    private static IWXAPI api;

    public static IWXAPI getIWXAPIInstance(Context context, String appId) {
        if (null == api) {
            api = WXAPIFactory.createWXAPI(context, appId, true);
            api.registerApp(appId);
        }

        return api;
    }

    public static IWXAPI getIWXAPIInstance() {
        return api;
    }
}
