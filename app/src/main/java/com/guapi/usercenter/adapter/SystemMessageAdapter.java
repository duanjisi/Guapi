package com.guapi.usercenter.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.RefreshOneMessageResponse;

import java.util.List;

import butterknife.Bind;

/**
 * author: long
 * date: ON 2017/7/19.
 */

public class SystemMessageAdapter extends RecyclerAdapter<RefreshOneMessageResponse.MsListBean> {
    public SystemMessageAdapter(List<RefreshOneMessageResponse.MsListBean> datas) {
        super(datas, R.layout.item_system);
    }

    @Override
    public BaseViewHolder holder(View view, int viewType) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<RefreshOneMessageResponse.MsListBean> {
        @Bind(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(RefreshOneMessageResponse.MsListBean object, int position) {
            if (object.getMs_type().equals("2")) {//2：瓜皮动态
                tvTitle.setText("动态");
            } else if (object.getMs_type().equals("3")) {//3：瓜皮评论
                tvTitle.setText("评论");
            } else if (object.getMs_type().equals("4")) {//4：瓜皮点赞
                tvTitle.setText("点赞");
            } else if (object.getMs_type().equals("6")) {//6关注消息
                tvTitle.setText("好友");
            }
            tvContent.setText(object.getMs_content());
            tvTime.setText(object.getMs_time());
            GlideUtil.loadPicture(object.getSms_user_imag_url(), ivAvatar, R.mipmap.logo);
        }
    }

}
