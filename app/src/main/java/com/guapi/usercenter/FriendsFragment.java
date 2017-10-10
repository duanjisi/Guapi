package com.guapi.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.ChooseFragmentActivityPageEvent;
import com.ewuapp.framework.common.utils.AppManager;
import com.ewuapp.framework.view.BasicFragment;
import com.ewuapp.framework.view.adapter.OnItemListener;
import com.ewuapp.framework.view.widget.refreshlayout.RefreshLayout;
import com.guapi.R;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.model.response.GetFriendsResponse;
import com.guapi.usercenter.adapter.FriendAdapter;
import com.library.im.EaseConstant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/21.
 */

public class FriendsFragment extends BasicFragment {
    @Bind(R.id.ref_layout)
    RefreshLayout refreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.ll_no_data)
    LinearLayout llNoData;
    @Bind(R.id.iv_no_data)
    ImageView ivNoData;
    @Bind(R.id.tv_no_data_remind)
    TextView tvNoDataRemind;
    @Bind(R.id.btn_no_data)
    Button btnNoData;

    FriendAdapter mAdapter;
    List<GetFriendsResponse.FriendListBean> mData = new ArrayList<>();
    String userId = "";

    public static FriendsFragment getIntance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", userId);
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("user_id", "");
        }
        refreshLayout.setEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new FriendAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItem(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(EaseConstant.EXTRA_USER_ID, mData.get(position).getDestHid());
                bundle.putString("user_id", mData.get(position).getDestUid());
                bundle.putString("destName", mData.get(position).getDestName());
                bundle.putString("from", "FriendActivity");
                startActivity(bundle, UserCenterActivity.class);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @OnClick({R.id.btn_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_no_data://邀请好友
                EventBus.getDefault().post(new ChooseFragmentActivityPageEvent(3));
                break;
        }
    }

    private void loadData() {
        addDisposable(Http.getFriends(context, 1, userId, new CallBack<GetFriendsResponse>() {
            @Override
            public void handlerSuccess(GetFriendsResponse data) {
                if (data.getFriendListBeans().size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    mData.clear();
                    mData.addAll(data.getFriendListBeans());
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (btnNoData == null)
                        return;
                    btnNoData.setText("邀请好友");
                    llNoData.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                    tvNoDataRemind.setText("您还没有好友，邀请更多好友~");
                }
            }

            @Override
            public void fail(int code, String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                if (code == 5000103) {
                    AppManager.getInstance().finishAll();
                    startActivity(null, LoginActivity.class);
                }
            }
        }));
    }

    @Override
    protected int getViewId() {
        return R.layout.refresh_listview;
    }

}
