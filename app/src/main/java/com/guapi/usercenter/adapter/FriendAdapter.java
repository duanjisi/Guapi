package com.guapi.usercenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.GetFriendsResponse;

import java.util.List;

import butterknife.Bind;

/**
 * author: long
 * date: ON 2017/6/21.
 */

public class FriendAdapter extends RecyclerAdapter<GetFriendsResponse.FriendListBean> {
    public FriendAdapter(List<GetFriendsResponse.FriendListBean> datas) {
        super(datas, R.layout.item_friends);
    }

    @Override
    public BaseViewHolder holder(View view, int viewType) {
        return new ViewHolder(view);
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
        @Bind(R.id.iv_sex)
        ImageView ivSex;
        @Bind(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(GetFriendsResponse.FriendListBean object, int position) {
            GlideUtil.loadPicture(object.getImageUrl(), ivAvatar);
            tvName.setText(object.getDestName());
            tvNote.setText(object.getNote());
            if (!CheckUtil.isNull(object.getAge())) {
                tvAge.setText(object.getAge() + "");
            }
            if (object.getSex() == 0) {//男
                ivSex.setImageResource(R.mipmap.boy_icon);
            } else {
                ivSex.setImageResource(R.mipmap.nhtb);
            }
            tvTime.setText(object.getLastTime());
        }
    }
}
