package com.guapi.usercenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.auth.BindPhoneActivity;
import com.guapi.auth.ChangePasswordActivity;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.tool.PreferenceKey;
//import com.guapi.util.GlideCatchUtil;
import com.guapi.util.GlideCatchUtil;
import com.hyphenate.EMCallBack;
import com.library.im.controller.HxHelper;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/21.
 */

public class SettingActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.iv_message_not_disturb)
    ImageView ivMesageNotDisturb;
    @Bind(R.id.iv_sound)
    ImageView ivSound;
    @Bind(R.id.iv_shock)
    ImageView ivShock;

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("设置");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvCache.setText(GlideCatchUtil.getInstance().getCacheSize());
        ivMesageNotDisturb.setSelected(Hawk.get(PreferenceKey.MESSAGE_NO_DISTURB, false));
        ivSound.setSelected(Hawk.get(PreferenceKey.SOUND, false));
        ivShock.setSelected(Hawk.get(PreferenceKey.SHOCK, false));
    }

    @OnClick({R.id.rl_change_password, R.id.rl_bind_phone, R.id.iv_message_not_disturb, R.id.iv_sound, R.id.iv_shock,
            R.id.rl_clear_cache, R.id.rl_about_us, R.id.rl_user_terms, R.id.btn_out_login})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_change_password://密码修改
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.rl_bind_phone://绑定手机
                startActivity(BindPhoneActivity.class);
                break;
            case R.id.iv_message_not_disturb://消息免打扰
                if (ivMesageNotDisturb.isSelected()) {
                    Hawk.put(PreferenceKey.MESSAGE_NO_DISTURB, false);
                    ivMesageNotDisturb.setSelected(false);
                } else {
                    Hawk.put(PreferenceKey.MESSAGE_NO_DISTURB, true);
                    ivMesageNotDisturb.setSelected(true);
                }
                break;
            case R.id.iv_sound://声音
                if (ivSound.isSelected()) {
                    ivSound.setSelected(false);
                    Hawk.put(PreferenceKey.SOUND, false);
                } else {
                    Hawk.put(PreferenceKey.SOUND, true);
                    ivSound.setSelected(true);
                }
                break;
            case R.id.iv_shock://震动
                if (ivShock.isSelected()) {
                    Hawk.put(PreferenceKey.SHOCK, false);
                    ivShock.setSelected(false);
                } else {
                    Hawk.put(PreferenceKey.SHOCK, true);
                    ivShock.setSelected(true);
                }
                break;
            case R.id.rl_clear_cache://清除缓存
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                tvCache.setText("0M");
                showMessage("清除缓存成功");
                break;
            case R.id.rl_about_us://关于我们
                startActivity(AboutUsActivity.class);
                break;
            case R.id.rl_user_terms://用户条款
                bundle.putString("title", "用户条款");
                bundle.putString("url", "file:///android_asset/index.html");
                startActivity(bundle, WebViewActivity.class);
                break;
            case R.id.btn_out_login://退出登录
                logout();
                break;
        }
    }


    private void logout() {
        addDisposable(Http.logout(context, new CallBack<Result>() {
            @Override
            public void handlerSuccess(Result data) {
                HxHelper.getInstance().logout(context, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Hawk.remove(PreferenceKey.HAS_LOGIN);
                        setResult(RESULT_OK);
                        finish();
                        startActivity(LoginActivity.class);
                    }

                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
            }

            @Override
            public void fail(int code, String message) {
                showMessage(message);
            }
        }));
    }

}
