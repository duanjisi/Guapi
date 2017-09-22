package com.guapi.usercenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
 * author: long
 * date: ON 2017/6/21.
 */

public class FriendActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_friend)
    ImageView ivFriend;
    @Bind(R.id.iv_follow)
    ImageView ivFollow;
    @Bind(R.id.iv_fans)
    ImageView ivFans;
    @Bind(R.id.iv_mail_list)
    ImageView ivMailList;

    private ViewPageAdapter adapter;
    private List<Fragment> list = new ArrayList<>();

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
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

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_friend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        list.add(new FriendsFragment());
        list.add(new FollowFragment());
        list.add(new FansFragment());
        list.add(new MailListFragment());
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

    @OnClick({R.id.ll_friend, R.id.ll_follow, R.id.ll_fans, R.id.ll_mail_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_friend:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_follow:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_fans:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_mail_list:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    public void setViewTabColor(int position) {
        ivFriend.setVisibility(View.INVISIBLE);
        ivFollow.setVisibility(View.INVISIBLE);
        ivFans.setVisibility(View.INVISIBLE);
        ivMailList.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                ivFriend.setVisibility(View.VISIBLE);
                break;
            case 1:
                ivFollow.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivFans.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivMailList.setVisibility(View.VISIBLE);
                break;
        }
    }

}
