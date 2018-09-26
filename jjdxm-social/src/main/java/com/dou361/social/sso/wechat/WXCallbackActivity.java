package com.dou361.social.sso.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dou361.social.WeChat;
import com.dou361.social.share.wechat.WeChatShareProxy;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
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
 * 描 述：微信授权，分享回调activity
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class WXCallbackActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        IWXAPI api = WeChat.getIWXAPIInstance();
        if (null != api)
            api.handleIntent(intent, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        IWXAPI api = WeChat.getIWXAPIInstance();
        if (null != api)
            api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp)
            WeChatSSOProxy.authComplete((SendAuth.Resp) resp);
        else if (resp instanceof SendMessageToWX.Resp)
            WeChatShareProxy.shareComplete((SendMessageToWX.Resp) resp);

        finish();
    }


}
