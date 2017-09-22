package com.guapi.usercenter.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.library.im.EaseConstant;
import com.library.im.cache.ContactsCacheUtils;
import com.library.im.cache.RecycleTextView;

import butterknife.Bind;

/**
 * 聊天会话页
 * Created by z on 2016/8/28.
 */
public class ChatActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.recycle_text_view)
    RecycleTextView recycleTextView;

    public static ChatActivity instance;
    public String hxname = "";

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
            hxname = bundle.getString(EaseConstant.EXTRA_USER_ID, "");
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        instance = this;
        ContactsCacheUtils.getInstance(this).loadText(hxname, recycleTextView);
        if (CheckUtil.isNull(hxname)) {
            finish();
            return;
        }
        ChatFragment chatFragment = new ChatFragment();
//        传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, hxname);
        chatFragment.setArguments(args);
        replaceFragment(R.id.fragment_container, hxname, chatFragment);
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
