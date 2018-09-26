package com.dou361.social.sso.wechat;

import android.content.Context;

import com.dou361.social.WeChat;
import com.dou361.social.model.SocialInfo;
import com.dou361.social.model.SocialToken;
import com.dou361.social.model.SocialUser;
import com.dou361.social.sso.SocialSSOProxy;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
 * 描 述：微信授权proxy
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class WeChatSSOProxy {

    private static IWXCallback callback;

    public static void login(Context context, IWXCallback callback, SocialInfo info) {
        if (!SocialSSOProxy.isTokenValid(context)) {
            WeChatSSOProxy.callback = callback;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = info.getWeChatScope();
            WeChat.getIWXAPIInstance(context, info.getWechatAppId()).sendReq(req);
        }
    }

    public static void authComplete(SendAuth.Resp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                callback.onGetCodeSuccess(resp.code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                callback.onCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                callback.onFailure(new Exception("BaseResp.ErrCode.ERR_AUTH_DENIED  errCode:"+resp.errCode+" errCode:"+resp.errCode));
                break;
        }
    }

    public static void getToken(final String code, final String getTokenUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format(getTokenUrl, code));
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    String result = inputStream2String(in);
                    try {
                        JSONObject info = new JSONObject(result);
                        //当前token的有效市场只有7200s，需要利用refresh_token去获取新token，考虑当前需要利用token的只有获取用户信息，手动设置token超时为30天
                        SocialToken token = new SocialToken(info.getString("openid"), info.getString("access_token"), info.getString("refresh_token"), /*info.getLong("expires_in")*/ 30 * 24 * 60 * 60);
                        token.setResult(result);
                        callback.onGetTokenSuccess(token);
                    } catch (JSONException e) {
                        callback.onFailure(e);
                    }
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        }).start();

    }

    public static void getUserInfo(final Context context, final String getUserInfoUrl, final SocialToken token) {
        if (SocialSSOProxy.isTokenValid(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(String.format(getUserInfoUrl, token.getToken(), token.getOpenId()));
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConn.getInputStream());

                        String result = inputStream2String(in);
                        try {
                            JSONObject info = new JSONObject(result);
                            String name = info.getString("nickname");
                            int gender = info.getInt("sex");
                            String icon = info.getString("headimgurl");

                            SocialUser user = new SocialUser(SocialUser.TYPE_WECHAT,
                                    name, icon, gender, token);
                            user.setResult(result);
                            callback.onGetUserInfoSuccess(user);
                        } catch (JSONException e) {
                            callback.onFailure(e);
                        }
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                }
            }).start();
        }
    }

    private static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}
