package com.guapi.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.guapi.MainActivity;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.response.GetCodeResponse;
import com.guapi.model.response.LoginResponse;
import com.guapi.tool.CheckUtil;
import com.guapi.tool.JPushUtil;
import com.guapi.tool.PreferenceKey;
import com.guapi.usercenter.WebViewActivity;
import com.library.im.controller.HxHelper;
import com.library.im.utils.PreferenceManager;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by long on 2017/6/17.
 */

public class RegisterActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_password_first)
    EditText etPasswordFirst;
    @Bind(R.id.et_password_twice)
    EditText etPasswordTwice;

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
        return R.layout.activity_register;
    }

    @OnClick({R.id.tv_get_code, R.id.btn_register, R.id.tv_register_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code://获取验证码
                getCode(etPhone.getText().toString());
                break;
            case R.id.btn_register://注册
                register(etPhone.getText().toString(), etPasswordFirst.getText().toString(), etPasswordTwice.getText().toString(), etCode.getText().toString());
                break;
            case R.id.tv_register_agreement://注册协议
                Bundle bundle = new Bundle();
                bundle.putString("title", "注册协议");
                bundle.putString("url", "file:///android_asset/index.html");
                startActivity(bundle, WebViewActivity.class);
                break;
        }
    }

    private void getCode(String phone) {
        if (CheckUtil.isNull(phone)) {
            showMessage("手机号不能为空");
            return;
        }
        if(phone.length()!=11){
            showMessage("请输入正确的手机号");
            return;
        }
        if(!CheckUtil.checkPhone(phone)){
            showMessage("请输入正确的手机号");
            return;
        }
        loadIngShow();
        addDisposable(Http.getCode(context, phone, new CallBack<GetCodeResponse>() {
            @Override
            public void handlerSuccess(GetCodeResponse data) {
                loadIngDismiss();
            }

            @Override
            public void fail(int code, String message) {
                if (code == 5000101) {
                    showMessage("该手机号码已经注册，请更换其它手机号码");
                }
                loadIngDismiss();
            }
        }));
    }

    private void register(String phone, String password1, String password2, String code) {
        if (CheckUtil.isNull(phone)) {
            showMessage("手机号不能为空");
            return;
        }
        if(phone.length()!=11){
            showMessage("请输入正确的手机号");
            return;
        }
        if(!CheckUtil.checkPhone(phone)){
            showMessage("请输入正确的手机号");
            return;
        }
        if (CheckUtil.isNull(code)) {
            showMessage("验证码不能为空");
            return;
        }
        if(code.length()!=4){
            showMessage("请输入正确的验证码");
            return;
        }
        if (CheckUtil.isNull(password1)) {
            showMessage("密码不能为空");
            return;
        }
        if (CheckUtil.isNull(password2)) {
            showMessage("密码不能为空");
            return;
        }
        if (!password1.equals(password2)) {
            showMessage("两次输入的密码不相同");
            return;
        }
        loadIngShow();
        addDisposable(Http.register(context, phone, password1, code, new CallBack<LoginResponse>() {
            @Override
            public void handlerSuccess(LoginResponse data) {
                loginHx(phone, password1, data);
                loadIngDismiss();
                Hawk.put(PreferenceKey.SESSIONID, data.getSessionId());
                Hawk.put(PreferenceKey.LoginResponse, data);
                setResult(RESULT_OK);
                finish();
                startActivity(MainActivity.class);
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
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
                    Hawk.put(PreferenceKey.AVATAR, data.getUser().getAvatarUrl());
                    PreferenceManager.getInstance().setUserAvatar(data.getUser().getAvatarUrl());
                    PreferenceManager.getInstance().setUserUId(data.getUser().getUid());
                    loadIngDismiss();
                    Hawk.put(PreferenceKey.SESSIONID, data.getSessionId());
                    Hawk.put(PreferenceKey.LoginResponse, data);
                    finish();
                    startActivity(MainActivity.class);
                } else {
                    loadIngDismiss();
                }
            }
        });
    }

}
