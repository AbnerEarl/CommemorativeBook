package com.dou361.social.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dou361.social.R;
import com.dou361.social.SocialSDK;
import com.dou361.social.model.SocialInfo;
import com.dou361.social.view.ShareButton;
import com.tencent.connect.common.Constants;

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
 * 描 述：社交授权activity
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialOauthActivity extends Activity {

    private static final String TAG = "SocialOauthActivity";
    private static boolean DEBUG = SocialSDK.isDebugModel();

    private SocialInfo info;
    private ShareButton llWeibo;
    private ShareButton llWeChat;
    private ShareButton llQQ;

    /**
     * type=0, 用户选择QQ或者微博登录
     * type=1，用户选择微信登录
     */
    private int type = 0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.jjdxm_social_activity_social_oauth);

        info = (SocialInfo) getIntent().getExtras().getSerializable("info");

        llWeibo = (ShareButton) findViewById(R.id.social_oauth_sb_weibo);
        llWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginWeibo(SocialOauthActivity.this, info);
            }
        });
        llWeChat = (ShareButton) findViewById(R.id.social_oauth_sb_wechat);
        llWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginWeChat(SocialOauthActivity.this, info);
                type = 1;
            }
        });
        llQQ = (ShareButton) findViewById(R.id.social_oauth_sb_qq);
        llQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginQQ(SocialOauthActivity.this, info);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.oauthWeiboCallback(SocialOauthActivity.this, requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            SocialSDK.oauthQQCallback(requestCode, resultCode, data);
        }

        if (type == 0) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (type == 1) {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.jjdxm_social_snack_out);
    }
}
