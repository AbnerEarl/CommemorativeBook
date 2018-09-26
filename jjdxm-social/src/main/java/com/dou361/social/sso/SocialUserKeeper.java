package com.dou361.social.sso;

import android.content.Context;
import android.content.SharedPreferences;

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
 * 创建日期：2016/6/27 23:02
 * <p>
 * 描 述：用户信息持久化
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialUserKeeper {

    private static final String PREFERENCE_NAME = "es_social_user";

    private static final String KEY_TYPE = "type";
    private static final String KEY_OPENID = "open_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPIRES_TIME = "expires_time";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_SIGNATURE = "signature";

    /**
     * 持久化用户信息
     *
     * @param context context
     * @param user    用户信息
     */
    protected static void writeSocialUser(Context context, SocialUser user) {
        if (user == null || context == null)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_TYPE, user.getType());
        editor.putString(KEY_OPENID, user.getToken().getOpenId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.putInt(KEY_GENDER, user.getGender());
        editor.putString(KEY_TOKEN, user.getToken().getToken());
        editor.putString(KEY_REFRESH_TOKEN, user.getToken().getRefreshToken());
        editor.putLong(KEY_EXPIRES_TIME, user.getToken().getExpiresTime());
        editor.putString(KEY_SIGNATURE, user.getDesc());
        editor.commit();
    }

    /**
     * 读取用户信息
     *
     * @param context context
     * @return 用户信息
     */
    protected static SocialUser readSocialUser(Context context) {
        if (context == null)
            return null;

        SocialUser user = new SocialUser();
        SocialToken token = new SocialToken();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user.setType(preferences.getInt(KEY_TYPE, 0));
        user.setName(preferences.getString(KEY_NAME, ""));
        user.setAvatar(preferences.getString(KEY_AVATAR, ""));
        user.setGender(preferences.getInt(KEY_GENDER, 0));
        user.setDesc(preferences.getString(KEY_SIGNATURE, ""));
        token.setOpenId(preferences.getString(KEY_OPENID, ""));
        token.setToken(preferences.getString(KEY_TOKEN, ""));
        token.setRefreshToken(preferences.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(preferences.getLong(KEY_EXPIRES_TIME, 0));
        user.setToken(token);

        return user;
    }

    /**
     * 清除用户信息
     *
     * @param context context
     */
    protected static void clear(Context context) {
        if (null == context)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
