package com.guapi.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.guapi.Constants;
import com.guapi.main.base.BaseActivity;
import com.guapi.model.ActionEntity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private String WEIXIN_APP_ID = "wxb3893f7dffa1bd57";
    /**
     * 分享到微信接口
     **/
    private IWXAPI mWxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWxApi = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, false);
        mWxApi.registerApp(WEIXIN_APP_ID);
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                showToast(result, true);
                EventBus.getDefault().post(new ActionEntity(Constants.Action.CLOSE_HIDE_ACTIVITY));
                //去地图刷新界面
                EventBus.getDefault().post(new ActionEntity("com.guapi.refresh.map"));
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "分享返回";
                break;
        }
        showToast(result, true);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}