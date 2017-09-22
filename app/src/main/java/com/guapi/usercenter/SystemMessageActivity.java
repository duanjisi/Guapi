package com.guapi.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.adapter.OnItemListener;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.ewuapp.framework.view.widget.refreshlayout.RefreshLayout;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.main.GPCommentActivity;
import com.guapi.model.response.RefreshOneMessageResponse;
import com.guapi.usercenter.adapter.SystemMessageAdapter;
import com.library.im.EaseConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * author: long
 * date: ON 2017/7/19.
 */

public class SystemMessageActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.ref_layout)
    RefreshLayout refreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.ll_no_message)
    LinearLayout llNoMessage;

    String msg_type = "";
    SystemMessageAdapter mAdapter;
    List<RefreshOneMessageResponse.MsListBean> mData = new ArrayList<>();

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            msg_type = bundle.getString("msg_type", "");
        }
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
        return R.layout.refresh_listview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        refreshLayout.setEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new SystemMessageAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        loadData();
        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItem(View view, int position) {
                Bundle bundle = new Bundle();
                if (msg_type.equals("1")) {//瓜皮调到消息详情
                    bundle.putSerializable("MsListBean", mData.get(position));
//                    startActivity(bundle, SystemMessageDetailActivity.class);
                } else if (msg_type.equals("7")) {
                    if (mData.get(position).getMs_type().equals("6")) {//动态中的 关注调到个人资料，
                        bundle.putString("from", "SystemMessageActivity");
                        bundle.putString("user_id", mData.get(position).getMs_user_id());
                        bundle.putString("destName", mData.get(position).getMs_user_name());
                        bundle.putString(EaseConstant.EXTRA_USER_ID, mData.get(position).getMs_user_hid());
                        startActivity(bundle, UserCenterActivity.class);
                    } else {//调到瓜皮详情
                        Log.e("跳转到瓜皮详情", "跳转到瓜皮详情");
                        bundle.putString("gpId", mData.get(position).getGp_id());
                        startActivity(bundle, GPCommentActivity.class);
                    }
                }
            }
        });
    }

    private void loadData() {
        addDisposable(Http.refresh(context, msg_type, new CallBack<RefreshOneMessageResponse>() {
            @Override
            public void handlerSuccess(RefreshOneMessageResponse data) {
                Log.e("lONG", data.toString());
                if (data.getMsListBeen().size() > 0) {
                    mData.addAll(data.getMsListBeen());
                    mAdapter.notifyDataSetChanged();
                } else {
                    llNoMessage.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void fail(int code, String message) {
                showMessage(message);
            }
        }));
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        if (msg_type.equals("1")) {
            toolBarView.setTitleText("瓜皮");
        } else if (msg_type.equals("7")) {
            toolBarView.setTitleText("动态");
        }
        toolBarView.setVisibility(View.VISIBLE);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

}
