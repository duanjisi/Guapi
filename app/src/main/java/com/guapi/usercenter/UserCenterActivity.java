package com.guapi.usercenter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.utils.AppManager;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.ewuapp.framework.view.widget.NGridView;
import com.ewuapp.framework.view.widget.banner.BannerLayout;
import com.guapi.R;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.model.response.LoginResponse;
import com.guapi.model.response.UserGetCountResponse;
import com.guapi.tool.PreferenceKey;
import com.guapi.usercenter.adapter.LabelAdapter;
import com.guapi.usercenter.adapter.LingAdapter;
import com.guapi.usercenter.chat.ChatActivity;
import com.library.im.EaseConstant;
import com.library.im.controller.HxHelper;
import com.library.im.utils.PreferenceManager;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/21.
 */

public class UserCenterActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.banner_layout)
    BannerLayout bannerLayout;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.gridview_label)
    NGridView nGridViewLabel;
    @Bind(R.id.gridview_ling)
    NGridView gridviewLing;
    @Bind(R.id.tv_label_size)
    TextView tvLabelSize;
    @Bind(R.id.tv_ling_size)
    TextView tvLingSize;
    @Bind(R.id.tv_hide_gp_size)
    TextView tvHideGpSize;
    @Bind(R.id.tv_find_gp_zise)
    TextView tvFindGpSize;
    @Bind(R.id.tv_focus_size)
    TextView tvFoucusSize;
    @Bind(R.id.tv_fans_size)
    TextView tvFansSize;
    @Bind(R.id.iv_edit_data)
    ImageView ivEditData;
    @Bind(R.id.iv_chat)
    ImageView ivChat;
    @Bind(R.id.iv_follow_or_not_follow)
    ImageView ivFollowOrNotFollow;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_sex)
    ImageView ivSex;

    private List<String> imagesUrls = new ArrayList<>();
    private List<Integer> imgRes = new ArrayList<>();
    String user_id = "";
    String from = "";
    String hxName = "";
    String destName = "";
    LabelAdapter labelAdapter;
    LingAdapter lingAdapter;

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
        return R.layout.activity_usercenter;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id", "");
            from = bundle.getString("from", "");
            destName = bundle.getString("destName", "");
            hxName = bundle.getString(EaseConstant.EXTRA_USER_ID, "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        tvName.setText(destName);
        if (from.equals("MainActivity")) {
            ivChat.setVisibility(View.GONE);
            ivEditData.setVisibility(View.VISIBLE);
            ivFollowOrNotFollow.setVisibility(View.GONE);
        } else if (from.equals("MainActivity_guapi")) {//从首页瓜皮 缩略图跳转过来
            ivChat.setVisibility(View.VISIBLE);
            ivEditData.setVisibility(View.GONE);
            ivFollowOrNotFollow.setVisibility(View.VISIBLE);
        } else {
            ivFollowOrNotFollow.setVisibility(View.VISIBLE);
            ivChat.setVisibility(View.VISIBLE);
            ivEditData.setVisibility(View.GONE);
        }
    }

    UserGetCountResponse.DataBean dataBean;

    public void initData(UserGetCountResponse.DataBean data) {
        if (!CheckUtil.isNull(data.getPic_file1_url())) {
            GlideUtil.loadPicture(data.getPic_file1_url(), ivAvatar);
        }
        if (!CheckUtil.isNull(data.getLableInfor())) {
            String[] labels = data.getLableInfor().split(",");
            tvLabelSize.setText(labels.length + "");
            List<String> labelsList = new ArrayList<>();
            labelsList = Arrays.asList(labels);
            labelAdapter = new LabelAdapter(context, labelsList);
            nGridViewLabel.setAdapter(labelAdapter);
        } else {
            tvLabelSize.setText("0");
        }
        if (!(CheckUtil.isNull(data.getSex())) && data.getSex().equals("0")) {//男
            GlideUtil.loadPicture("", ivSex, R.mipmap.boy_icon);
        } else if (!(CheckUtil.isNull(data.getSex())) && data.getSex().equals("1")) {//女
            GlideUtil.loadPicture("", ivSex, R.mipmap.nhtb);
        }
        if (!CheckUtil.isNull(data.getLingInfor())) {
            String[] lings = data.getLingInfor().split(",");
            tvLabelSize.setText(lings.length + "");
            List<String> lingList = new ArrayList<>();
            lingList = Arrays.asList(lings);
            lingAdapter = new LingAdapter(context, lingList);
            gridviewLing.setAdapter(lingAdapter);
        } else {
            tvLingSize.setText("0");
        }
        tvHideGpSize.setText(data.getMyGpCount() + "");
        tvFindGpSize.setText(data.getFindGpCount() + "");
        tvFoucusSize.setText(data.getmFocusCount() + "");
        tvFansSize.setText(data.getfFocusCount() + "");
        GlideUtil.loadPicture(data.getAvatarUrl(), ivAvatar);
        tvAge.setText(data.getAge());
        if (!CheckUtil.isNull(data.getNickname())) {
            tvNickName.setText(data.getNickname());
            tvName.setText(data.getNickname());
        }
        if (!CheckUtil.isNull(data.getNote())) {
            tvSign.setText(data.getNote());
        }
        imagesUrls.clear();
        if (!CheckUtil.isNull(data.getPic_file1_url())) {
            imagesUrls.add(data.getPic_file1_url());
        }
        if (!CheckUtil.isNull(data.getPic_file2_url())) {
            imagesUrls.add(data.getPic_file2_url());
        }
        if (!CheckUtil.isNull(data.getPic_file3_url())) {
            imagesUrls.add(data.getPic_file3_url());
        }
        if (!CheckUtil.isNull(data.getPic_file4_url())) {
            imagesUrls.add(data.getPic_file4_url());
        }
        if (!CheckUtil.isNull(data.getPic_file5_url())) {
            imagesUrls.add(data.getPic_file5_url());
        }
        if (!CheckUtil.isNull(data.getPic_file6_url())) {
            imagesUrls.add(data.getPic_file6_url());
        }
        if (imagesUrls.size() > 0) {
            bannerLayout.setViewUrls(imagesUrls);
        } else {
            imgRes.add(R.mipmap.logo);
            bannerLayout.setViewRes(imgRes);
        }
        if (data.getIsFocus().equals("Y")) {//已关注
            ivFollowOrNotFollow.setSelected(true);
        } else if (data.getIsFocus().equals("N")) {//未关注
            ivFollowOrNotFollow.setSelected(false);
        }
    }

    private void loadUserData() {
        addDisposable(Http.getCount(context, user_id, new CallBack<UserGetCountResponse>() {
            @Override
            public void handlerSuccess(UserGetCountResponse data) {
                dataBean = data.getDataBean();
                initData(data.getDataBean());
                if (from.equals("MainActivity")) {
                    Hawk.put(PreferenceKey.AVATAR, dataBean.getAvatarUrl());
                    HxHelper.avatar = Hawk.get(PreferenceKey.AVATAR, "");
                    PreferenceManager.getInstance().setUserAvatar(dataBean.getAvatarUrl());
                    LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
                    LoginResponse.UserBean userBean = loginResponse.getUser();
                    userBean.setAvatarUrl(dataBean.getAvatarUrl());
                    userBean.setPic_file1_url(dataBean.getPic_file1_url());
                    userBean.setPic_file2_url(dataBean.getPic_file2_url());
                    userBean.setPic_file3_url(dataBean.getPic_file3_url());
                    userBean.setPic_file4_url(dataBean.getPic_file4_url());
                    userBean.setPic_file5_url(dataBean.getPic_file5_url());
                    userBean.setPic_file6_url(dataBean.getPic_file6_url());
                    userBean.setSex(data.getDataBean().getSex());
                    userBean.setAge(data.getDataBean().getAge());
                    userBean.setNickname(data.getDataBean().getNickname());
                    loginResponse.setUser(userBean);
                    Hawk.put(PreferenceKey.LoginResponse, loginResponse);
                }
            }

            @Override
            public void fail(int code, String message) {
                showMessage(message);
                if (code == 5000103) {
                    AppManager.getInstance().finishAll();
                    startActivity(null, LoginActivity.class);
                }
            }
        }));
    }

    @OnClick({R.id.iv_edit_data, R.id.iv_back, R.id.iv_chat, R.id.iv_follow_or_not_follow})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_edit_data:
                bundle.putSerializable("DataBean", dataBean);
                startActivity(bundle, EditDataActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_chat:
                bundle.putString(EaseConstant.EXTRA_USER_ID, hxName);
                startActivity(bundle, ChatActivity.class);
                break;
            case R.id.iv_follow_or_not_follow:
                if (ivFollowOrNotFollow.isSelected()) {//已经关注该用户
                    //取消关注
                    addDisposable(Http.removeFocus(context, user_id, new CallBack<Result>() {
                        @Override
                        public void handlerSuccess(Result data) {
                            showMessage("取消关注成功");
                            loadUserData();
                        }

                        @Override
                        public void fail(int code, String message) {
                            showMessage(message);
                        }
                    }));
                } else {//未关注
                    //关注
                    addDisposable(Http.add(context, user_id, new CallBack<Result>() {
                        @Override
                        public void handlerSuccess(Result data) {
                            showMessage("关注成功");
                            loadUserData();
                        }

                        @Override
                        public void fail(int code, String message) {
                            showMessage(message);
                        }
                    }));
                }
                break;
        }
    }

}
