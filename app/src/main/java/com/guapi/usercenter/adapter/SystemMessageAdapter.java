package com.guapi.usercenter.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.RefreshOneMessageResponse;
import com.guapi.tool.DateUtil;
import com.guapi.tool.Utils;

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
            if (!CheckUtil.isNull(object.getMs_time())) {
                tvTime.setText(getTimeStr(object.getMs_time()));
            }
            tvTime.setText(object.getMs_time());
            GlideUtil.loadPicture(object.getSms_user_imag_url(), ivAvatar, R.mipmap.logo);
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
