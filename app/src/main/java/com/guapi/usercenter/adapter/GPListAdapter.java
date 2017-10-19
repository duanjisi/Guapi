package com.guapi.usercenter.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.QueryFocusGpResponse;
import com.guapi.tool.DateUtil;
import com.guapi.tool.PreferenceKey;
import com.guapi.tool.Utils;
import com.guapi.widget.scan.RoundImageView;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.Bind;

/**
 * Created by long on 2017/10/9.
 */

public class GPListAdapter extends RecyclerAdapter<QueryFocusGpResponse.GpListBean> {
    public GPListAdapter(List<QueryFocusGpResponse.GpListBean> datas) {
        super(datas, R.layout.item_gp_list);
    }

    @Override
    public BaseViewHolder holder(View view, int viewType) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<QueryFocusGpResponse.GpListBean> {
        @Bind(R.id.iv_user)
        CircleImageView ivUser;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_sex)
        TextView tvSex;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_hb)
        RoundImageView ivHb;
        @Bind(R.id.tv_message)
        TextView tvMessage;
        @Bind(R.id.tv_remind)
        TextView tvRemind;
        @Bind(R.id.tv_see)
        TextView tvSee;
        @Bind(R.id.tv_zan)
        TextView tvZan;
        @Bind(R.id.tv_liuyan)
        TextView tvLiuYan;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_distance)
        TextView tvDistance;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(QueryFocusGpResponse.GpListBean object, int position) {
            if (object.getUser_imag_url() != null) {
                GlideUtil.loadPicture(object.getUser_imag_url(), ivUser);
            }
            if (object.getUser_name() != null) {
                tvName.setText(object.getUser_name());
            }
            tvSex.setText(object.getAge());
            Drawable nav_up = null;
            if (object.getSex().equals("1")) {
                nav_up = context.getResources().getDrawable(R.drawable.ic_sex_gilr);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else {
                nav_up = context.getResources().getDrawable(R.drawable.ic_sex_gilr);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            tvSex.setCompoundDrawables(nav_up, null, null, null);
            if (!CheckUtil.isNull(object.getCreate_time())) {
                tvTime.setText(getTimeStr(object.getCreate_time()));
            }
            GlideUtil.loadPicture(object.getKey_file_url(), ivHb);
            tvMessage.setText(object.getNote());
            tvRemind.setText(object.getDesc());
            tvZan.setText(object.getGrant_total() + "");
            tvSee.setText(object.getView_total() + "");
            tvLiuYan.setText(object.getComment_total() + "");
            tvAddress.setText(object.getLine());
            LatLng latLng = Hawk.get(PreferenceKey.LOCATION_LATLNG, null);
            if (latLng != null) {
                float v = AMapUtils.calculateLineDistance(latLng, new LatLng(Double.valueOf(object.getLat()), Double.valueOf(object.getLng())));
                tvDistance.setText("距离" + getDistance(v));
            } else {
                tvDistance.setText("距离10km");
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

    private String getDistance(float ad) {
        String distance = "";
        if (ad > 1000) {
            int k = (int) ad / 1000;
            int m = (int) (ad % 1000) / 100;
            if (m != 0) {
                distance = k + "." + m + " km";
            } else {
                distance = k + " km";
            }
        } else {
            distance = (int) ad + " m";
        }
        return distance;
    }

}
