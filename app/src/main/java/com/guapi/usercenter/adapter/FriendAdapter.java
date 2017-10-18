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
import com.guapi.tool.DateUtil;
import com.guapi.tool.Utils;

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
            if (!CheckUtil.isNull(object.getLastTime())) {
                tvTime.setText(getTimeStr(object.getLastTime()));
            }
        }
    }

    public String getTimeStr(String time) {
        //create_time : 2017-09-23 19:11:59.0
        String[] timeStr1 = time.split(" ");//timeStr1[0]=2017-09-23,timeStr[1]=19:11:59.0
        String[] timeStr2 = timeStr1[0].split("-");//timeStr2[0]=2017,timeStr2[1]=09,timeStr2[2]=23
        String monthStr = "";
        String dayStr = "";
        if (timeStr2[1].startsWith("0")) {
            monthStr = timeStr2[1].substring(0);
        } else {
            monthStr = timeStr2[1];
        }
        if (timeStr2[2].startsWith("0")) {
            dayStr = timeStr2[2].substring(1);
        } else {
            dayStr = timeStr2[2];
        }
        String str = "";
        long[] times = Utils.getDistanceTimes(time, Utils.getDate("" + (System.currentTimeMillis() / 1000)));
        if (DateUtil.isSameDay(DateUtil.formatDateMills(time, DateUtil.yyyy_MMddHHmmss))) {
            if (times[3] < 1) {
                if (times[4] < 1) {
                    if (times[5] < 1) {
                        str = times[5] + "秒前";
                    } else {
                        str = "刚才";
                    }
                } else {
                    str = times[4] + "分钟前";
                }
            } else {
                str = times[3] + "小时前";
            }
        } else {
            str = timeStr2[0] + "年" + monthStr + "月" + dayStr + "日";
        }
        return str;
    }
}
