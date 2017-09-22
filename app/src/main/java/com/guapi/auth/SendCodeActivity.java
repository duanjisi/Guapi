package com.guapi.auth;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.MsgEvent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/27.
 */

public class SendCodeActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.tv_remind)
    TextView tvRemind;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;

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

    @Subscribe
    public void onEventMainThread(MsgEvent event) {
        loadIngDismiss();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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
        return R.layout.activity_send_code;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("发送验证码");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_get_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                genCodePswd(Hawk.get(PreferenceKey.PHONE, ""));
                break;
            case R.id.btn_next:
                if (!CheckUtil.isNull(etCode.getText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("code", etCode.getText().toString());
                    startActivity(bundle, ChangePasswordActivity.class);
                } else {
                    showMessage("请先输入验证码");
                }
                break;
        }
    }

    public void genCodePswd(String phone) {
        loadIngShow();
        addDisposable(Http.genCodePswd(context, phone, new CallBack<GetCodeResponse>() {
            @Override
            public void handlerSuccess(GetCodeResponse data) {
                loadIngDismiss();
                mCountDownTimer.start();
                tvRemind.setText("验证码短信已发送至：\\n18300072223");
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
