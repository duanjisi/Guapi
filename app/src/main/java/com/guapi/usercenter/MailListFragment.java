package com.guapi.usercenter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.view.BasicFragment;
import com.ewuapp.framework.view.adapter.OnItemListener;
import com.ewuapp.framework.view.widget.refreshlayout.RefreshLayout;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.bean.PhoneInFo;
import com.guapi.model.response.GetFriendsResponse;
import com.guapi.usercenter.adapter.MailListAdapter;
import com.guapi.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * author: long
 * date: ON 2017/6/21.
 */
public class MailListFragment extends BasicFragment {
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

    MailListAdapter mAdapter;
    List<GetFriendsResponse.FriendListBean> mData = new ArrayList<>();
    private static final int REQUEST_CONTACT_PERMISSIONS = 931;

    @Override
    protected int getViewId() {
        return R.layout.refresh_listview;
    }

    @Override
    protected void init() {
        refreshLayout.setEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new MailListAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItem(View view, int position) {
                sendInvite(mData.get(position).getPhone(), mData.get(position).getDestName());
            }
        });
    }

    public void sendInvite(String phone, String name) {
        addDisposable(Http.sendInvite(context, phone, name, new CallBack<Result>() {
            @Override
            public void handlerSuccess(Result data) {
                ToastUtil.show(context, "短信邀请成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void fail(int code, String message) {
                ToastUtil.show(context, "短信邀请失败", Toast.LENGTH_SHORT);
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        getContactsPermission();
    }

    public void getContactsPermission() {
        final String[] permissions = {
                Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
        final List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CONTACT_PERMISSIONS);
        } else {
            List<PhoneInFo> phoneInFos = getContacts();//通讯录中联系人信息
            for (int i = 0; i < phoneInFos.size(); i++) {
                GetFriendsResponse.FriendListBean friendListBean = new GetFriendsResponse.FriendListBean();
                friendListBean.setType(2);
                friendListBean.setDestName(phoneInFos.get(i).name);
                friendListBean.setPhone(phoneInFos.get(i).phone);
                mData.add(friendListBean);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<PhoneInFo> phoneInFos = getContacts();//通讯录中联系人信息
        if (grantResults.length != 0) {
            for (int i = 0; i < phoneInFos.size(); i++) {
                GetFriendsResponse.FriendListBean friendListBean = new GetFriendsResponse.FriendListBean();
                friendListBean.setType(2);
                friendListBean.setDestName(phoneInFos.get(i).name);
                friendListBean.setPhone(phoneInFos.get(i).phone);
                mData.add(friendListBean);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<PhoneInFo> getContacts() {
        List<PhoneInFo> list = new ArrayList<>();
        ContentResolver resolver = getActivity().getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        while (phoneCursor.moveToNext()) {
            //读取通讯录的姓名
            String name = phoneCursor.getString(phoneCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneInFo phoneInfo = new PhoneInFo(name, number);
            list.add(phoneInfo);
        }
        return list;
    }
}
