package com.guapi.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ewuapp.framework.common.http.ChooseFragmentActivityPageEvent;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.usercenter.adapter.ViewPageAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by long on 2017/10/10.
 */

public class FansAndFollowsActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_follow)
    ImageView ivFollow;
    @Bind(R.id.iv_fans)
    ImageView ivFans;

    private ViewPageAdapter adapter;
    private List<Fragment> list = new ArrayList<>();
    String userId = "";

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
            userId = bundle.getString("user_id", "");
            Log.e("FansAndFollowsuser_id", userId);
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("好友");
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
        return R.layout.activity_fans_follow;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        list.add(FollowFragment.getIntance(userId));
        list.add(FansFragment.getIntance(userId));
        adapter = new ViewPageAdapter(getSupportFragmentManager(), list);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setViewTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe
    public void onEventMainThread(ChooseFragmentActivityPageEvent event) {
        viewPager.setCurrentItem(event.page);
    }

    @OnClick({R.id.ll_follow, R.id.ll_fans})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_follow:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_fans:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public void setViewTabColor(int position) {
        ivFollow.setVisibility(View.INVISIBLE);
        ivFans.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                ivFollow.setVisibility(View.VISIBLE);
                break;
            case 1:
                ivFans.setVisibility(View.VISIBLE);
                break;
        }
    }

}
