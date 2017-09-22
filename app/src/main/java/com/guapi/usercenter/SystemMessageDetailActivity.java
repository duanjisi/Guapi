package com.guapi.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.model.response.RefreshOneMessageResponse;

import butterknife.Bind;

/**
 * Created by long on 2017/9/8.
 */

public class SystemMessageDetailActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;

    RefreshOneMessageResponse.MsListBean msListBean;

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
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            msListBean = (RefreshOneMessageResponse.MsListBean) bundle.getSerializable("MsListBean");
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_system_message_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTime.setText(msListBean.getMs_time());
        tvContent.setText(msListBean.getMs_content());
        toolBarView.setVisibility(View.VISIBLE);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setDrawable(ToolBarView.TEXT_RIGHT,R.mipmap.dttb);
    }
}
