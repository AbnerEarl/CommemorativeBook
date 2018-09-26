package com.dou361.social.share.wechat;

import android.content.Context;

import com.dou361.social.SocialUtils;
import com.dou361.social.WeChat;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

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
 * 描 述：微信分享Proxy
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class WeChatShareProxy {

    private static IWXShareCallback mCallback;

    public static void shareToWeChat(final Context context, final String appId, final String title, final String desc,
                                     final String url, final String thumbnail, final IWXShareCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WeChatShareProxy.mCallback = callback;
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = desc;
                byte[] thumb = SocialUtils.getHtmlByteArray(thumbnail);
                if (null != thumb)
                    msg.thumbData = SocialUtils.compressBitmap(thumb, 32);
                else
                    msg.thumbData = SocialUtils.compressBitmap(SocialUtils.getDefaultShareImage(context), 32);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = SocialUtils.buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                WeChat.getIWXAPIInstance(context, appId).sendReq(req);
            }
        }).start();

    }

    public static void shareToWeChatTimeline(final Context context, final String appId, final String title, final String url,
                                             final String thumbnail, final IWXShareCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WeChatShareProxy.mCallback = callback;
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                byte[] thumb = SocialUtils.getHtmlByteArray(thumbnail);
                if (null != thumb)
                    msg.thumbData = SocialUtils.compressBitmap(thumb, 32);
                else
                    msg.thumbData = SocialUtils.compressBitmap(SocialUtils.getDefaultShareImage(context), 32);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = SocialUtils.buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                WeChat.getIWXAPIInstance(context, appId).sendReq(req);
            }
        }).start();

    }

    public static void shareComplete(SendMessageToWX.Resp resp) {
        if (null != mCallback) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    mCallback.onSuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    mCallback.onCancel();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                default:
                    mCallback.onFailure(new Exception("BaseResp.ErrCode.ERR_AUTH_DENIED  errCode:"+resp.errCode+" errCode:"+resp.errCode));
                    break;
            }
        }
    }
}
