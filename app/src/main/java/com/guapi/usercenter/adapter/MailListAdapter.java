package com.guapi.usercenter.adapter;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.GetFriendsResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * author: long
 * date: ON 2017/6/21.
 */

public class MailListAdapter extends RecyclerAdapter<GetFriendsResponse.FriendListBean> {
    List<GetFriendsResponse.FriendListBean> datas = new ArrayList<>();

    public MailListAdapter(List<GetFriendsResponse.FriendListBean> datas) {
        super(datas, R.layout.item_friends);
        this.datas = datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
            ViewHolder holder = (ViewHolder) holder(view, viewType);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_mail_list, parent, false);
            MailListViewHolder holder = (MailListViewHolder) holder(view, viewType);
            return holder;
        }
    }

    @Override
    public BaseViewHolder holder(View view, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(view);
        } else {
            return new MailListViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    class MailListViewHolder extends BaseViewHolder<GetFriendsResponse.FriendListBean> {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_send_message)
        TextView tvSendMessage;

        public MailListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(GetFriendsResponse.FriendListBean object, int position) {
            tvName.setText(object.getDestName());
            tvSendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    class ViewHolder extends BaseViewHolder<GetFriendsResponse.FriendListBean> {
        @Bind(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_age)
        TextView tvAge;
        @Bind(R.id.tv_note)
        TextView tvNote;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(GetFriendsResponse.FriendListBean object, int position) {
            GlideUtil.loadPicture(object.getImageUrl(), ivAvatar);
            tvName.setText(object.getDestName());
            tvNote.setText(object.getNote());
            tvAge.setText(object.getAge() + "");
            if (object.getSex() == 0) {//男

            } else {

            }
        }
    }
}
