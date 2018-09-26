package com.example.frank.commemorativebook.social.share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.dou361.social.SocialSDK;
import com.dou361.social.model.SocialToken;
import com.dou361.social.model.SocialUser;
import com.dou361.social.otto.BusProvider;
import com.dou361.social.otto.SSOBusEvent;
import com.example.frank.commemorativebook.R;
import com.squareup.otto.Subscribe;
import com.tencent.connect.common.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SsoActivity extends BaseActivity {

    private static final String TAG = "SsoActivity";

    @Bind(R.id.sso_btn_login_qq)
    Button btnLoginQQ;
    @Bind(R.id.sso_btn_logout_qq)
    Button btnLogoutQQ;
    @Bind(R.id.sso_btn_login_wechat)
    Button btnLoginWeChat;
    @Bind(R.id.sso_btn_logout_wechat)
    Button btnLogoutWeChat;
    @Bind(R.id.sso_btn_login_weibo)
    Button btnLoginWeibo;
    @Bind(R.id.sso_btn_logout_weibo)
    Button btnLogoutWeibo;

    @OnClick({R.id.sso_btn_login_qq, R.id.sso_btn_login_wechat, R.id.sso_btn_login_weibo})
    public void login(View view) {
        switch (view.getId()) {
            case R.id.sso_btn_login_weibo:
                SocialSDK.setDebugMode(true);
                SocialSDK.initWeibo("1633462674");
                SocialSDK.oauthWeibo(SsoActivity.this);
                break;

            case R.id.sso_btn_login_wechat:
                SocialSDK.setDebugMode(true);
                SocialSDK.initWeChat("wx3ecc7ffe590fd845", "1b3f07fa99d82232d360c359f6504980");
                SocialSDK.oauthWeChat(SsoActivity.this);
                break;

            case R.id.sso_btn_login_qq:
                SocialSDK.setDebugMode(true);
                SocialSDK.initQQ("1104664609");
                SocialSDK.oauthQQ(SsoActivity.this);
                break;

        }
    }

    @OnClick({R.id.sso_btn_logout_qq, R.id.sso_btn_logout_wechat, R.id.sso_btn_logout_weibo})
    public void logout(View view) {
        switch (view.getId()) {
            case R.id.sso_btn_logout_weibo:
                SocialSDK.revokeWeibo(SsoActivity.this);
                break;

            case R.id.sso_btn_logout_wechat:
                SocialSDK.revokeWeChat(SsoActivity.this);
                break;

            case R.id.sso_btn_logout_qq:
                SocialSDK.revokeQQ(SsoActivity.this);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sso);
        ButterKnife.bind(this);


        /**************************************************/
        BusProvider.getInstance().register(this);
        /**************************************************/
    }

    @Subscribe
    public void onOauthResult(SSOBusEvent event) {
        switch (event.getType()) {
            case SSOBusEvent.TYPE_GET_TOKEN:
                SocialToken token = event.getToken();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_TOKEN " + token.toString());
                break;
            case SSOBusEvent.TYPE_GET_USER:
                SocialUser user = event.getUser();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_USER " + user.toString());
                break;
            case SSOBusEvent.TYPE_FAILURE:
                Exception e = event.getException();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_FAILURE " + e.toString());
                break;
            case SSOBusEvent.TYPE_CANCEL:
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_CANCEL");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        /*********************************************/
        BusProvider.getInstance().unregister(this);
        /*********************************************/
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.oauthWeiboCallback(SsoActivity.this, requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            SocialSDK.oauthQQCallback(requestCode, resultCode, data);
        }

    }
}
