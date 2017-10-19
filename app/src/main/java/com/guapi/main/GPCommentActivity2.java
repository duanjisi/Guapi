package com.guapi.main;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Constants;
import com.ewuapp.framework.common.utils.CompatUtil;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.common.utils.SharedPre;
import com.ewuapp.framework.common.utils.StringFormat;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.HorizontalDividerItemDecoration;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.ewuapp.framework.view.widget.VerticalDividerItemDecoration;
import com.google.gson.Gson;
import com.guapi.R;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.main.adapter.CommentAdapter;
import com.guapi.model.response.DoGPResponse;
import com.guapi.model.response.GPResponse;
import com.guapi.model.response.LoginResponse;
import com.guapi.tool.Global;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
 * @since 2017/7/11 0011
 */

public class GPCommentActivity2 extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {

    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.rv_comment)
    RecyclerView rvComment;
    @Bind(R.id.et_comment)
    EditText etComment;

    View headView;
    TextView tvCommentCount;
    TextView tvLY;

    private CommentAdapter commentAdapter;
    private GPResponse.GpListBean bean;
    private ImageLoader imageLoader;

    private String gpId;

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_gp_comment;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        gpId = getIntent().getExtras().getString("gpId", "");

        bean = (GPResponse.GpListBean) getIntent().getSerializableExtra(Global.KEY_OBJ);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initCommentList();
    }

    private void initCommentList() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(bean.getCommentList());
        rvComment.setAdapter(commentAdapter);

        if (TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
            headView = LayoutInflater.from(this).inflate(R.layout.layout_comment_hb_top, null);
            ImageView ivHB = (ImageView) headView.findViewById(R.id.iv_hb);
            ivHB.setOnClickListener(v -> {
                loadIngShow();
                Http.doGP(GPCommentActivity2.this, bean.getGpId(), Global.TYPE_COMPETE, "", new CallBack<DoGPResponse>() {
                    @Override
                    public void handlerSuccess(DoGPResponse data) {
                        loadIngDismiss();
                        // // TODO: 2017/7/11 0011 抢红包之后的操作
                        ToastUtils.showLong("您已抢到" + data.getData().getValue() + "元");
//                            LoginResponse user = new Gson().fromJson(SharedPre.getString(Global.KEY_USER_OBJ, ""), LoginResponse.class);
//                            GPResponse.GpListBean.CommentListBean bean = new GPResponse.GpListBean.CommentListBean();
//                            bean.setCommentInfo(data.getData().getValue());
//                            bean.setCommentTime(TimeUtils.getNowString());
//                            bean.setCommentUserId(user.getUser().getUid());
//                            bean.setCommentUserName(user.getUser().getNickname());
//                            bean.setCommentUserImagUrl(user.getUser().getAvatarUrl());
//                            commentAdapter.addData(bean);
//                            tvCommentCount.setText(StringFormat.formatForRes(R.string.comment_count, commentAdapter.getData().size()));
//                            tvLY.setText(String.valueOf(commentAdapter.getData().size()));
                    }

                    @Override
                    public void fail(int code, String message) {
                        loadIngDismiss();
                        ToastUtils.showLong(message);
                    }
                });
            });
        } else {
            headView = LayoutInflater.from(this).inflate(R.layout.layout_comment_top, null);
            RecyclerView rvPic = (RecyclerView) headView.findViewById(R.id.rv_pic);
            rvPic.setLayoutManager(new GridLayoutManager(this, 3));

            Paint paint = new Paint();
            paint.setStrokeWidth(2);
            paint.setColor(CompatUtil.getColor(this, android.R.color.transparent));
            rvPic.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
            rvPic.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).paint(paint).build());

//            PicAdapter picAdapter = new PicAdapter(getPicArray(), bean.getType());
//            rvPic.setAdapter(picAdapter);
        }
        TextView tvUserName = (TextView) headView.findViewById(R.id.tv_name);
        TextView tvTime = (TextView) headView.findViewById(R.id.tv_time);
        TextView tvMessage = (TextView) headView.findViewById(R.id.tv_message);
        TextView tvZan = (TextView) headView.findViewById(R.id.tv_zan);
        tvLY = (TextView) headView.findViewById(R.id.tv_liuyan);
        TextView tv_sex = (TextView) headView.findViewById(R.id.tv_sex);

        tvZan.setOnClickListener(v -> {
            loadIngShow();
            Http.doGP(GPCommentActivity2.this, bean.getGpId(), Global.TYPE_GRANT, "", new CallBack<DoGPResponse>() {
                @Override
                public void handlerSuccess(DoGPResponse data) {
                    loadIngDismiss();
                    int zanCount = Integer.valueOf(tvZan.getText().toString()) + 1;
                    tvZan.setText(String.valueOf(zanCount));
                }

                @Override
                public void fail(int code, String message) {
                    loadIngDismiss();
                    ToastUtils.showLong(message);
                }
            });
        });

        tvCommentCount = (TextView) headView.findViewById(R.id.tv_comment_count);
        CircleImageView ivUser = (CircleImageView) headView.findViewById(R.id.iv_user);

        tv_sex.setText(bean.getAge());
        tvUserName.setText(bean.getUserName());
//        tvTime.setText(bean.getCreateTime());
        tvMessage.setText(bean.getDesc());
        tvZan.setText(bean.getGrantTotal());
        tvLY.setText(bean.getCommentTotal());

        String time = bean.getCreateTime();
        if (!TextUtils.isEmpty(time)) {
            if (time.contains(".0")) {
                time = time.substring(0, time.indexOf(".") - 1);
            }
            tvTime.setText(time);
        }
        String sex = bean.getSex();
        Drawable nav_up = null;
        if (sex.equals("1")) {
            nav_up = getResources().getDrawable(R.drawable.ic_sex_gilr);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        } else {
            nav_up = getResources().getDrawable(R.drawable.ic_sex_gilr);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        }
        tv_sex.setCompoundDrawables(nav_up, null, null, null);
        tvCommentCount.setText(StringFormat.formatForRes(R.string.comment_count, bean.getCommentTotal()));
//        Glide.with(this).load(bean.getUserImagUrl()).bitmapTransform(new GlideCircleTransform(this)).into(ivUser);
        imageLoader.displayImage(bean.getUserImagUrl(), ivUser, ImageLoaderUtils.getDisplayImageOptions());
        commentAdapter.addHeaderView(headView);
    }

    private List<String> getPicArray() {
        List<String> array = new ArrayList<>();
        addPic(array, bean.getPicFile1Url());
        addPic(array, bean.getPicFile2Url());
        addPic(array, bean.getPicFile3Url());
        addPic(array, bean.getPicFile4Url());
        addPic(array, bean.getPicFile5Url());
        addPic(array, bean.getPicFile6Url());
        addPic(array, bean.getPicFile7Url());
        addPic(array, bean.getPicFile8Url());
        addPic(array, bean.getPicFile9Url());
        return array;
    }

    private void addPic(List<String> array, String pic) {
        if (!TextUtils.isEmpty(pic)) {
            array.add(pic);
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        if (TextUtils.equals(bean.getType(), Global.TYPE_PIC)) {
            toolBarView.setTitleText("照片瓜皮");
        } else if (TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
            toolBarView.setTitleText("红包瓜皮");
        }
        toolBarView.setBackPressed(this);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
    }

    @OnClick({R.id.iv_face, R.id.iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_face:
                break;
            case R.id.iv_send:
                if (TextUtils.isEmpty(etComment.getText())) {
                    return;
                }
                String message = etComment.getText().toString();
                loadIngShow();
                Http.doGP(this, bean.getGpId(), Global.TYPE_COMMENT, message, new CallBack<DoGPResponse>() {
                    @Override
                    public void handlerSuccess(DoGPResponse data) {
                        loadIngDismiss();

                        LoginResponse user = new Gson().fromJson(SharedPre.getString(Global.KEY_USER_OBJ, ""), LoginResponse.class);
                        GPResponse.GpListBean.CommentListBean bean = new GPResponse.GpListBean.CommentListBean();
                        bean.setCommentInfo(message);
                        bean.setCommentTime(TimeUtils.getNowString());
                        bean.setCommentUserId(user.getUser().getUid());
                        bean.setCommentUserName(user.getUser().getNickname());
                        bean.setCommentUserImagUrl(user.getUser().getAvatarUrl());
                        commentAdapter.addData(bean);
                        tvCommentCount.setText(StringFormat.formatForRes(R.string.comment_count, commentAdapter.getData().size()));
                        tvLY.setText(String.valueOf(commentAdapter.getData().size()));
                    }

                    @Override
                    public void fail(int code, String message) {
                        loadIngDismiss();
                        ToastUtils.showLong(message);
                        if (code == Constants.NET_CODE_NEED_LOGIN) {
                            IntentUtil.startActivity(GPCommentActivity2.this, LoginActivity.class, false);
                        }
                    }
                });
                break;
        }
    }
}
