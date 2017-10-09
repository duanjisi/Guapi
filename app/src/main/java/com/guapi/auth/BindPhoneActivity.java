package com.guapi.auth;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.utils.AppManager;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.response.GetCodeResponse;
import com.guapi.tool.CheckUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/19.
 */

public class BindPhoneActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;

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
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("绑定手机");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_getcode, R.id.btn_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                genCodePswd(etPhone.getText().toString());
                break;
            case R.id.btn_bind:
                bindPhone(etCode.getText().toString(), etPhone.getText().toString());
                break;
        }
    }

    public void bindPhone(String code, String phone) {
        if (CheckUtil.isNull(code)) {
            showMessage("请输入验证码");
            return;
        }
        if (CheckUtil.isNull(phone)) {
            showMessage("请输入手机号");
            return;
        }
        loadIngShow();
        addDisposable(Http.registerPhone(context, code, phone, new CallBack<Result>() {
            @Override
            public void handlerSuccess(Result data) {
                loadIngDismiss();
                showMessage("更换手机号成功");
                AppManager.getInstance().finishAll();
                startActivity(LoginActivity.class);
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
                showMessage(message);
            }
        }));
    }

    public void genCodePswd(String phone) {
        if (CheckUtil.isNull(phone)) {
            showMessage("请输入手机号");
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
                loadIngDismiss();
                showMessage(message);
            }
        }));
    }

}
