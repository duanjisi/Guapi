package com.guapi.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guapi.R;
import com.guapi.model.response.GPResponse;
import com.guapi.tool.Utils;
import com.guapi.usercenter.UserCenterActivity;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

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
 * @since 2017/7/11 0011
 */

public class CommentAdapter extends BaseQuickAdapter<GPResponse.GpListBean.CommentListBean, BaseViewHolder> {

    private ImageLoader imageLoader;
    private Resources resources;
    private Context mcontext;

    public CommentAdapter(@Nullable List<GPResponse.GpListBean.CommentListBean> data) {
        super(R.layout.item_comment, data);
        imageLoader = ImageLoaderUtils.createImageLoader(mContext);
        resources = mContext.getResources();
    }

    public CommentAdapter(Context context, @Nullable List<GPResponse.GpListBean.CommentListBean> data) {
        super(R.layout.item_comment, data);
        imageLoader = ImageLoaderUtils.createImageLoader(mContext);
        resources = context.getResources();
        this.mcontext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GPResponse.GpListBean.CommentListBean item) {
        CircleImageView imageView = helper.getView(R.id.iv_user);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tv_sex = helper.getView(R.id.tv_sex);
//        Glide.with(helper.itemView.getContext()).load(item.getCommentUserImagUrl()).bitmapTransform(new GlideCircleTransform(helper.itemView.getContext())).into(imageView);
        imageLoader.displayImage(item.getCommentUserImagUrl(), imageView, ImageLoaderUtils.getDisplayImageOptions());
        String time = item.getCommentTime();
        if (!TextUtils.isEmpty(time)) {
            if (time.contains(".0")) {
                time = time.substring(0, time.indexOf("."));
            }
            tvTime.setText(Utils.getTimeStr(time));
        }
        int sex = item.getSex();
        Drawable nav_up = null;
        if (sex == 1) {
            nav_up = resources.getDrawable(R.drawable.ic_sex_gilr);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        } else {
            nav_up = resources.getDrawable(R.drawable.boy_icon);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        }
        tv_sex.setCompoundDrawables(nav_up, null, null, null);
        tv_sex.setText("" + item.getAge());
        helper.setText(R.id.tv_name, item.getCommentUserName())
                .setText(R.id.tv_message, item.getCommentInfo());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("user_id", item.getCommentUserId());
                intent.putExtra("from", "GPCommentActivity_list_guapi");
                intent.putExtra("destName", item.getCommentUserName());
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, bean.getHxid());
                mContext.startActivity(intent);
            }
        });
    }
}
