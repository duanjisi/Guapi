package com.guapi.auth;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.guapi.tool.PreferenceKey;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/19.
 */

public class ChangePasswordActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_phone)
    EditText etPhone;

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {//一分钟，间隔一秒
        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setEnabled(false);
            tvGetCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText("重新发送");
            tvGetCode.setEnabled(true);
        }
    };

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("修改密码");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_change_password;
    }

    @OnClick({R.id.btn_confirm, R.id.tv_get_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                changePassword(etPhone.getText().toString(), etNewPassword.getText().toString(), etCode.getText().toString());
                break;
            case R.id.tv_get_code:
                genCodePswd(etPhone.getText().toString());
                break;
        }
    }

    public void changePassword(String phone, String newPSWD, String code) {
        if (CheckUtil.isNull(phone)) {
            showMessage("请输入手机号");
            return;
        }
        if (CheckUtil.isNull(code)) {
            showMessage("请输入验证码");
            return;
        }
        if (CheckUtil.isNull(newPSWD)) {
            showMessage("请输入新密码");
            return;
        }
        loadIngShow();
        addDisposable(Http.changePwd(context, phone, newPSWD, code, new CallBack<Result>() {
            @Override
            public void handlerSuccess(Result data) {
                loadIngDismiss();
                Hawk.put(PreferenceKey.PASSWORD, newPSWD);
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
        addDisposable(Http.genCodePswd(context, phone, new CallBack<GetCodeResponse>() {
            @Override
            public void handlerSuccess(GetCodeResponse data) {
                loadIngDismiss();
                mCountDownTimer.start();
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
                showMessage(message);
            }
        }));
    }

}
