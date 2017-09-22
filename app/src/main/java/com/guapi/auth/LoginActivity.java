package com.guapi.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.utils.SharedPre;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.google.gson.Gson;
import com.guapi.MainActivity;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.response.LoginResponse;
import com.guapi.tool.CheckUtil;
import com.guapi.tool.Global;
import com.guapi.tool.JPushUtil;
import com.guapi.tool.PreferenceKey;
import com.library.im.controller.HxHelper;
import com.library.im.utils.PreferenceManager;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by long on 2017/6/17.
 */

public class LoginActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;

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
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etPhone.setText(Hawk.get(PreferenceKey.PHONE, ""));
        etPassword.setText(Hawk.get(PreferenceKey.PASSWORD, ""));
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登陆
                login(etPhone.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.btn_register://注册
                startForResult(null, FINSH_ACTIVITY, RegisterActivity.class);
                break;
            case R.id.tv_forget_password://忘记密码
                startActivity(null, ChangePasswordActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == FINSH_ACTIVITY) {
            finish();
        }
    }

    private void login(final String phone, final String password) {
        if (CheckUtil.isNull(phone)) {
            showMessage("手机号不能为空");
            return;
        }
        if (CheckUtil.isNull(password)) {
            showMessage("密码不能为空");
            return;
        }
        loadIngShow();
        addDisposable(Http.login(context, phone, password, new CallBack<LoginResponse>() {
            @Override
            public void handlerSuccess(LoginResponse data) {
                SharedPre.putString(Global.KEY_USER_OBJ, new Gson().toJson(data));
                loginHx(phone, password, data);
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
                showMessage(message);
            }
        }));
    }

    private void loginHx(String phone, String password, LoginResponse data) {
        JPushUtil.get().setAlias(data.getUser().getHid());
        HxHelper.getInstance().login(data.getUser().getHid(), data.getUser().getHpass(), new HxHelper.CallBack() {
            @Override
            public void callBack(boolean state) {
                if (state) {
                    Log.e("Login", "登陆环信成功");
                    PreferenceManager.getInstance().setUserUId(data.getUser().getUid());
                    Hawk.put(PreferenceKey.PASSWORD, password);
                    Hawk.put(PreferenceKey.PHONE, phone);
                    loadIngDismiss();
                    Hawk.put(PreferenceKey.SESSIONID, data.getSessionId());
                    Hawk.put(PreferenceKey.HAS_LOGIN, true);
                    Hawk.put(PreferenceKey.LoginResponse, data);
                    Hawk.put(PreferenceKey.AVATAR, data.getUser().getAvatarUrl());
                    PreferenceManager.getInstance().setUserAvatar(data.getUser().getAvatarUrl());
                    finish();
                    startActivity(MainActivity.class);
                } else {
                    loadIngDismiss();
                }
            }
        });
    }
}
