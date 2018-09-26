package com.dou361.social.sso;

import android.content.Context;
import android.content.SharedPreferences;

import com.dou361.social.model.SocialInfo;

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
 * 创建日期：2016/6/27 22:56
 * <p>
 * 描 述：社交平台信息持久化
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialInfoKeeper {

    private static final String PREFERENCE_NAME = "es_social_info";

    private static final String KEY_DEBUG = "debug";
    private static final String KEY_WECHAT_APP_ID = "wechat_app_id";
    private static final String KEY_WECHAT_APP_SECRET = "wechat_app_secret";
    private static final String KEY_WECHAT_SCOPE = "wechat_scope";
    private static final String KEY_WEIBO_APP_KEY = "weibo_app_key";
    private static final String KEY_WEIBO_REDIRECT_URL = "weibo_redirect_url";
    private static final String KEY_WEIBO_SCOPE = "weibo_scope";
    private static final String KEY_QQ_APP_ID = "qq_app_id";
    private static final String KEY_QQ_SCOPE = "qq_scope";

    /**
     * 持久化社交平台信息
     *
     * @param context context
     * @param info    社交平台信息
     */
    public static void writeSocialInfo(Context context, SocialInfo info) {
        if (info == null || context == null)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DEBUG, info.isDebugMode());
        editor.putString(KEY_WECHAT_APP_ID, info.getWechatAppId());
        editor.putString(KEY_WECHAT_APP_SECRET, info.getWeChatAppSecret());
        editor.putString(KEY_WECHAT_SCOPE, info.getWeChatScope());
        editor.putString(KEY_WEIBO_APP_KEY, info.getWeiboAppKey());
        editor.putString(KEY_WEIBO_REDIRECT_URL, info.getWeiboRedirectrUrl());
        editor.putString(KEY_WEIBO_SCOPE, info.getWeiboScope());
        editor.putString(KEY_QQ_SCOPE, info.getQqScope());
        editor.putString(KEY_QQ_APP_ID, info.getQqAppId());
        editor.commit();
    }

    /**
     * 读取社交平台信息
     *
     * @param context context
     * @return 社交平台信息
     */
    public static SocialInfo readSocialInfo(Context context) {
        if (context == null)
            return null;

        SocialInfo info = new SocialInfo();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        info.setDebugMode(preferences.getBoolean(KEY_DEBUG, false));
        info.setWechatAppId(preferences.getString(KEY_WECHAT_APP_ID, ""));
        info.setWeChatAppSecret(preferences.getString(KEY_WECHAT_APP_SECRET, ""));
        info.setWeChatScope(preferences.getString(KEY_WECHAT_SCOPE, "snsapi_userinfo"));
        info.setWeiboAppKey(preferences.getString(KEY_WEIBO_APP_KEY, ""));
        info.setWeiboRedirectrUrl(preferences.getString(KEY_WEIBO_REDIRECT_URL, "http://www.sina.com"));
        info.setWeiboScope(preferences.getString(KEY_WEIBO_SCOPE, "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write"));
        info.setQqAppId(preferences.getString(KEY_QQ_APP_ID, ""));
        info.setQqScope(preferences.getString(KEY_QQ_SCOPE, "all"));

        return info;
    }

    /**
     * 清除社交平台信息
     *
     * @param context context
     */
    public static void clear(Context context) {
        if (null == context)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
