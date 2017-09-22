package com.guapi.auth;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.guapi.MainActivity;
import com.guapi.R;
import com.guapi.tool.PreferenceKey;
import com.orhanobut.hawk.Hawk;

/**
 * Created by long on 2017/6/17.
 */
public class SplashActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
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
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        boolean firstIn = Hawk.get(PreferenceKey.FIRST_IN, true);
        if (firstIn) {
            startActivity(null, GuideActivity.class);
            finish();
            return;
        } else {
            startHandler();
        }
    }

    private void startHandler() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Bundle bundle = new Bundle();
                if (Hawk.get(PreferenceKey.HAS_LOGIN, false)) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                finish();
            }
        }, 2000L);
    }
}
