package com.guapi.usercenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.response.RefreshOneMessageResponse;
import com.guapi.usercenter.chat.ConversationFragment;
import com.library.im.widget.EaseConversationList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/7/10.
 */

public class MessageActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.iv_guapi)
    CircleImageView ivG;
    @Bind(R.id.tv_guapi_content)
    TextView tvGContent;
    @Bind(R.id.tv_guapi_time)
    TextView tvGTime;

    @Bind(R.id.tv_dongtai_content)
    TextView tvDongTaiContent;
    @Bind(R.id.tv_dongtai_time)
    TextView tvDongTaiTime;
    @Bind(R.id.iv_dongtai)
    CircleImageView ivDongtai;

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
        return R.layout.activity_message;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("消息中心");
        toolBarView.setVisibility(View.VISIBLE);
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
        ConversationFragment conversationFragment = ConversationFragment.getIntance();
        replaceFragment(R.id.fragment_container, "___", conversationFragment);
        loadData();//动态消息
    }

    private void loadData() {
        addDisposable(Http.refreshOne(context, "", new CallBack<RefreshOneMessageResponse>() {
            @Override
            public void handlerSuccess(RefreshOneMessageResponse data) {
                if (data != null && data.getMsListBeen().size() > 0) {
                    for (int i = 0; i < data.getMsListBeen().size(); i++) {
                        if (data.getMsListBeen().get(i).getMs_type().equals("1")) {
                            if(CheckUtil.isNull(data.getMsListBeen().get(i).getMs_content())){
                                tvGContent.setText("暂无消息");
                            }else {
                                tvGContent.setText(data.getMsListBeen().get(i).getMs_content());
                            }
                            tvGTime.setText(data.getMsListBeen().get(i).getMs_time());
                        } else if (data.getMsListBeen().get(i).getMs_type().equals("7")) {
                            if(CheckUtil.isNull(data.getMsListBeen().get(i).getMs_content())){
                                tvDongTaiContent.setText("暂无消息");
                            }else {
                                tvDongTaiContent.setText(data.getMsListBeen().get(i).getMs_content());
                            }
                            tvDongTaiTime.setText(data.getMsListBeen().get(i).getMs_time());
                        }
                    }
                }
            }

            @Override
            public void fail(int code, String message) {
                showMessage(message);
            }
        }));
    }

    @OnClick({R.id.ll_dongtai, R.id.ll_guapi})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_dongtai:
                bundle.putString("msg_type", "7");
                startActivity(bundle, SystemMessageActivity.class);
                break;
            case R.id.ll_guapi:
                bundle.putString("msg_type", "1");
                startActivity(bundle, SystemMessageActivity.class);
                break;
        }
    }

    protected void replaceFragment(int containerViewId, String tag, Fragment fragment) {
        boolean isAdd = true;
        Fragment tempFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (tempFragment == null) {
            tempFragment = fragment;
            isAdd = false;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, tempFragment, tag);
        if (!isAdd) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }
}
