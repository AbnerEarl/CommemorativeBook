package com.dou361.social.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.dou361.social.R;
import com.dou361.social.SocialSDK;
import com.dou361.social.model.SocialInfo;
import com.dou361.social.model.SocialShareScene;
import com.dou361.social.otto.BusProvider;
import com.dou361.social.otto.ShareBusEvent;
import com.dou361.social.view.ShareButton;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
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
 * 创建日期：2016/6/27 22:45
 * <p>
 * 描 述：一键社会化分享
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SocialShareActivity extends Activity implements IWeiboHandler.Response {

    private SocialInfo info;
    private SocialShareScene scene;

    private ShareButton sbWechat;
    private ShareButton sbWeChatTimeline;
    private ShareButton sbWeibo;
    private ShareButton sbQQ;
    private ShareButton sbQZone;
    private ShareButton sbMore;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.jjdxm_social_activity_social_share);

        getWindow().setGravity(Gravity.BOTTOM);

        info = (SocialInfo) getIntent().getExtras().getSerializable("info");
        scene = (SocialShareScene) getIntent().getExtras().getSerializable("scene");

        initViews();
    }

    private void initViews() {
        sbWechat = (ShareButton) findViewById(R.id.social_share_sb_wechat);
        sbWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_WECHAT);
                SocialSDK.shareToWeChat(SocialShareActivity.this, info.getWechatAppId(), scene);
            }
        });
        sbWeChatTimeline = (ShareButton) findViewById(R.id.social_share_sb_wechat_timeline);
        sbWeChatTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSDK.shareToWeChatTimeline(SocialShareActivity.this, info.getWechatAppId(), scene);
                scene.setType(SocialShareScene.SHARE_TYPE_WECHAT_TIMELINE);
            }
        });
        sbWeibo = (ShareButton) findViewById(R.id.social_share_sb_weibo);
        sbWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_WEIBO);
                SocialSDK.shareToWeibo(SocialShareActivity.this, info.getWeiboAppKey(), scene);
            }
        });
        sbQQ = (ShareButton) findViewById(R.id.social_share_sb_qq);
        sbQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_QQ);
                SocialSDK.shareToQQ(SocialShareActivity.this, info.getQqAppId(), scene);
            }
        });
        sbQZone = (ShareButton) findViewById(R.id.social_share_sb_qzone);
        sbQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_QZONE);
                SocialSDK.shareToQZone(SocialShareActivity.this, info.getQqAppId(), scene);
            }
        });
        sbMore = (ShareButton) findViewById(R.id.social_share_sb_more);
        sbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_DEFAULT);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, scene.getTitle() + "\n\r" + scene.getUrl());
                share.putExtra(Intent.EXTRA_TITLE, scene.getTitle());
                share.putExtra(Intent.EXTRA_SUBJECT, scene.getDesc());
                startActivity(share);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.jjdxm_social_snack_out);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (scene.getType() == SocialShareScene.SHARE_TYPE_WEIBO) {
            SocialSDK.shareToWeiboCallback(intent, this);
            finish();
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, scene.getType(), scene.getId()));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_CANCEL, scene.getType()));
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), new Exception("WBConstants.ErrorCode.ERR_FAIL: "
                        + baseResponse.errMsg)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_QZONE_SHARE || requestCode == Constants.REQUEST_QQ_SHARE) {
            SocialSDK.shareToQCallback(requestCode, resultCode, data);
            finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (scene.getType() == SocialShareScene.SHARE_TYPE_WECHAT || scene.getType() == SocialShareScene.SHARE_TYPE_WECHAT_TIMELINE) {
            finish();
        }
    }

}
