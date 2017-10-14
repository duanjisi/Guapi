package com.guapi.usercenter.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.adapter.BaseViewHolder;
import com.ewuapp.framework.view.adapter.RecyclerAdapter;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.guapi.R;
import com.guapi.model.response.QueryFocusGpResponse;
import com.guapi.tool.PreferenceKey;
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
        ImageView ivHb;
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
//            if (object.getComment_list() != null && object.getComment_list().size() > 0 && object.getComment_list().get(0).getComment_user_imag_url() != null) {
//                GlideUtil.loadPicture(object.getComment_list().get(0).getComment_user_imag_url(), ivUser);
//            }
            if (object.getUser_imag_url() != null) {
                GlideUtil.loadPicture(object.getUser_imag_url(), ivUser);
            }
//            if (object.getComment_list() != null&& object.getComment_list().size() > 0  && object.getComment_list().get(0) != null && object.getComment_list().get(0).getComment_user_name() != null) {
//                tvName.setText(object.getComment_list().get(0).getComment_user_name());
//            }
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
            tvTime.setText(object.getCreate_time());
            GlideUtil.loadPicture(object.getKey_file_url(), ivHb);
            tvMessage.setText(object.getNote());
            tvRemind.setText(object.getDesc());
            tvZan.setText(object.getGrant_total() + "");
            tvSee.setText(object.getView_total() + "");
            tvLiuYan.setText(object.getComment_total() + "");
            tvAddress.setText(object.getLine());

            LatLng latLng = Hawk.get(PreferenceKey.LOCATION_LATLNG, null);
            if (latLng != null) {
                float v = com.amap.api.maps2d.AMapUtils.calculateArea(latLng, new LatLng(Double.valueOf(object.getLat()), Double.valueOf(object.getLng())));
                tvDistance.setText("距离" + v);
            }
        }
    }


}
