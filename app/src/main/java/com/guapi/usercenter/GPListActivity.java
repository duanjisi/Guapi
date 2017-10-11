package com.guapi.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.adapter.OnItemListener;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.main.GPCommentActivity;
import com.guapi.model.response.LoginResponse;
import com.guapi.model.response.QueryFocusGpResponse;
import com.guapi.tool.PreferenceKey;
import com.guapi.usercenter.adapter.GPListAdapter;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by long on 2017/10/9.
 */

public class GPListActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    GPListAdapter mAdapter;
    List<QueryFocusGpResponse.GpListBean> mData = new ArrayList<>();
    String user_id = "";
    String type = "";

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id", "");
            type = bundle.getString("type", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        return R.layout.activity_gp_list;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("瓜皮列表");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new GPListAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        loadData();
        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItem(View view, int position) {
                LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
                if (user_id.equals("") || user_id.equals(loginResponse.getUser().getUid())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("gpId", mData.get(position).getGp_id());
                    startActivity(bundle, GPCommentActivity.class);
                } else {

                }
            }
        });
    }

    public void loadData() {
        addDisposable(Http.queryFocusGp(context, type, user_id, new CallBack<QueryFocusGpResponse>() {
            @Override
            public void handlerSuccess(QueryFocusGpResponse data) {
                if (data.getGpListBeans().size() > 0) {
                    mData.clear();
                    mData.addAll(data.getGpListBeans());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void fail(int code, String message) {
                showMessage(message);
            }
        }));
    }

}
