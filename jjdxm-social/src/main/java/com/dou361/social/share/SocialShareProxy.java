package com.dou361.social.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dou361.social.R;
import com.dou361.social.SocialSDK;
import com.dou361.social.model.SocialInfo;
import com.dou361.social.model.SocialShareScene;
import com.dou361.social.otto.BusProvider;
import com.dou361.social.otto.ShareBusEvent;
import com.dou361.social.share.qq.QQShareProxy;
import com.dou361.social.share.wechat.IWXShareCallback;
import com.dou361.social.share.wechat.WeChatShareProxy;
import com.dou361.social.share.weibo.AccessTokenKeeper;
import com.dou361.social.share.weibo.WeiboShareProxy;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
 * 创建日期：2016/6/27 22:50
 * <p>
 * 描 述：社会化分享代理
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialShareProxy {

    private static final String TAG = "SocialShareProxy";
    private static boolean DEBUG = SocialSDK.isDebugModel();

    private static SocialShareScene scene;

    public static void share(Context context, SocialInfo info, SocialShareScene scene) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        bundle.putSerializable("scene", scene);
        intent.putExtras(bundle);
        intent.setClass(context, SocialShareActivity.class);
        context.startActivity(intent);

        ((Activity) context).overridePendingTransition(R.anim.jjdxm_social_snack_in, 0);
    }

    private static IWXShareCallback wechatShareCallback = new IWXShareCallback() {
        @Override
        public void onSuccess() {
            if (DEBUG)
                Log.i(TAG, "SocialShareProxy#wechatShareCallback onSuccess");
            BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, scene.getType(), scene.getId()));
        }

        @Override
        public void onCancel() {
            if (DEBUG)
                Log.i(TAG, "SocialShareProxy#wechatShareCallback onCancel");
            BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_CANCEL, scene.getType()));
        }

        @Override
        public void onFailure(Exception e) {
            if (DEBUG)
                Log.e(TAG, "SocialShareProxy#wechatShareCallback onFailure");
            BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), e));
        }
    };

    /**
     * 分享到微信
     *
     * @param context context
     * @param appId   app id
     * @param scene   场景
     */
    public static void shareToWeChat(final Context context, String appId, final SocialShareScene scene) {
        if (DEBUG)
            Log.i(TAG, "SocialShareProxy#shareToWeChat");
        SocialShareProxy.scene = scene;
        WeChatShareProxy.shareToWeChat(context, appId, scene.getTitle(), scene.getDesc(), scene.getUrl(),
                scene.getThumbnail(), wechatShareCallback);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param context context
     * @param appId   app id
     * @param scene   场景
     */
    public static void shareToWeChatTimeline(Context context, String appId, final SocialShareScene scene) {
        if (DEBUG)
            Log.i(TAG, "SocialShareProxy#shareToWeChatTimeline");
        SocialShareProxy.scene = scene;
        WeChatShareProxy.shareToWeChatTimeline(context, appId, scene.getTitle(), scene.getUrl(),
                scene.getThumbnail(), wechatShareCallback);
    }

    /**
     * 分享到微博
     *
     * @param context     context
     * @param appKey      app key
     * @param redirectUrl 回调地址
     * @param scene       场景
     */
    public static void shareToWeibo(final Context context, String appKey, String redirectUrl, final SocialShareScene scene) {
        if (DEBUG)
            Log.i(TAG, "SocialShareProxy#shareToWeibo");
        WeiboShareProxy.shareTo(context, appKey, redirectUrl, scene.getTitle(), scene.getDesc(),
                scene.getThumbnail(), scene.getUrl(), new WeiboAuthListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        if (DEBUG)
                            Log.i(TAG, "SocialShareProxy#shareToWeibo onComplete");
                        Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
                        if (token.isSessionValid())
                            AccessTokenKeeper.writeAccessToken(context, token);
//                        BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, scene.getType(), scene.getId()));
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        if (DEBUG)
                            Log.i(TAG, "SocialShareProxy#shareToWeibo onWeiboException " + e.toString());
//                        BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), e));
                    }

                    @Override
                    public void onCancel() {
                        if (DEBUG)
                            Log.i(TAG, "SocialShareProxy#shareToWeibo onCancel");
//                        BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_CANCEL, scene.getType()));
                    }
                });

    }


    private static IUiListener qShareListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (DEBUG)
                Log.i(TAG, "SocialShareProxy#qShareListener onComplete");
            if (scene == null) {
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, 0, -1));
            } else {
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, scene.getType(), scene.getId()));
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (DEBUG)
                Log.i(TAG, "SocialShareProxy#qShareListener onError :" + uiError.errorCode + " "
                        + uiError.errorMessage + " " + uiError.errorDetail);
            BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), new Exception(uiError.errorCode + " "
                    + uiError.errorMessage + " " + uiError.errorDetail)));
        }

        @Override
        public void onCancel() {
            if (DEBUG)
                Log.i(TAG, "SocialShareProxy#qShareListener onCancel");
            BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_CANCEL, scene.getType()));
        }
    };

    /**
     * 分享到QQ
     *
     * @param context context
     * @param appId   app id
     * @param scene   场景
     */
    public static void shareToQQ(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.scene = scene;
        QQShareProxy.shareToQQ(context, appId, scene.getTitle(), scene.getDesc(), scene.getUrl(),
                scene.getThumbnail(), scene.getAppName(), qShareListener);
    }


    /**
     * 分享到QQ空间
     *
     * @param context context
     * @param appId   app id
     * @param scene   场景
     */
    public static void shareToQZone(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.scene = scene;
        QQShareProxy.shareToQZone(context, appId, scene.getTitle(), scene.getDesc(), scene.getUrl(),
                scene.getThumbnail(), qShareListener);
    }

    /**
     * 分享到QQ，QQ空间结果回调
     *
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
    public static void shareToQCallback(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qShareListener);
    }

    /**
     * 分享到微博结果回调
     *
     * @param intent   intent
     * @param response response
     */
    public static void shareToWeiboCallback(Intent intent, IWeiboHandler.Response response) {
        WeiboShareProxy.getInstance().handleWeiboResponse(intent, response);
    }
}
