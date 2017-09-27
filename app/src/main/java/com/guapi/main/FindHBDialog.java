package com.guapi.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewuapp.framework.view.widget.BaseDialog2;
import com.guapi.R;
import com.guapi.model.response.GPResponse;
import com.guapi.tool.Global;
import com.guapi.tool.Utils;
import com.guapi.usercenter.UserCenterActivity;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CircleImageView;
import com.library.im.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/7/10 0010
 */

public class FindHBDialog extends BaseDialog2 {

    @Bind(R.id.iv_user)
    CircleImageView ivUser;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.tv_see)
    TextView tvSee;
    @Bind(R.id.tv_zan)
    TextView tvZan;
    @Bind(R.id.tv_liuyan)
    TextView tvLiuyan;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_dist)
    TextView tvDist;

    GPResponse.GpListBean bean;
    @Bind(R.id.iv_temp)
    ImageView ivTemp;
    @Bind(R.id.iv_play)
    ImageView iv_play;

    private Context context;
    private ImageLoader imageLoader;
    private Resources resources;

    public FindHBDialog(Context context, GPResponse.GpListBean bean) {
        super(context, R.style.base_dialog, true);
        this.bean = bean;
        this.context = context;
        this.resources = context.getResources();
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_find_hb;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);
//        tvSex.setVisibility(View.GONE);
        if (!TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
//            Glide.with(getContext()).load(bean.getPicFile1Url())
//                    .bitmapTransform(new GlideCircleTransform(getContext()))
//                    .placeholder(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivTemp);
            if (TextUtils.equals(bean.getType(), Global.TYPE_PIC)) {
                Glide.with(getContext()).load(bean.getPicFile1Url()).into(ivTemp);
                iv_play.setVisibility(View.GONE);
            } else {
                Glide.with(getContext()).load(bean.getVideo_pic()).into(ivTemp);
                iv_play.setVisibility(View.VISIBLE);
            }
        }
//        Glide.with(getContext()).load(bean.getUserImagUrl()).into(ivUser);
        imageLoader.displayImage(bean.getUserImagUrl(), ivUser, ImageLoaderUtils.getDisplayImageOptions());
        int sex = bean.getSex();
        Drawable nav_up = null;
        if (sex == 1) {
            nav_up = resources.getDrawable(R.drawable.ic_sex_gilr);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        } else {
            nav_up = resources.getDrawable(R.drawable.boy_icon);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        }
        tvSex.setCompoundDrawables(nav_up, null, null, null);
        tvSex.setText(bean.getAge());
        tvMessage.setText(bean.getDesc());
        tvName.setText(bean.getUserName());
//        tvDist.setText(StringFormat.formatForRes(R.string.hb_dis, bean.distance));
        tvDist.setText(getDistance(bean.distance));
        String time = bean.getCreateTime();
        if (!TextUtils.isEmpty(time)) {
            if (time.contains(".0")) {
                time = time.substring(0, time.indexOf("."));
            }
            tvTime.setText(Utils.getTimeStr(time));
        }
        tvLiuyan.setText(bean.getCommentTotal());
        tvZan.setText(bean.getGrantTotal());
        tvLocation.setText(bean.getLine());
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = d.getWidth();
        lp.height = d.getHeight();
        Window window = getWindow();
        window.setAttributes(lp);
        window.setBackgroundDrawableResource(R.color.result_view);
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

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_see, R.id.iv_close, R.id.tv_zan, R.id.tv_liuyan, R.id.iv_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_see:
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_zan:
                break;
            case R.id.tv_liuyan:
                break;
            case R.id.iv_user:
                Intent intent = new Intent(context, UserCenterActivity.class);
                intent.putExtra("user_id", bean.getUserId());
                intent.putExtra("from", "MainActivity_guapi");
                intent.putExtra("destName", bean.getUserName());
                intent.putExtra(EaseConstant.EXTRA_USER_ID, bean.getUser_hid());
                context.startActivity(intent);
                break;
        }
    }
}
