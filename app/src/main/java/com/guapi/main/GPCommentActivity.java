package com.guapi.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Constants;
import com.ewuapp.framework.common.utils.CompatUtil;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.common.utils.StringFormat;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.HorizontalDividerItemDecoration;
import com.ewuapp.framework.view.widget.VerticalDividerItemDecoration;
import com.guapi.R;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.main.adapter.CommentAdapter;
import com.guapi.main.adapter.PicAdapter;
import com.guapi.model.PicEntity;
import com.guapi.model.response.DoGPResponse;
import com.guapi.model.response.GPResponse;
import com.guapi.model.response.GpSingleRespone;
import com.guapi.model.response.LoginResponse;
import com.guapi.tool.Global;
import com.guapi.tool.PreferenceKey;
import com.guapi.usercenter.UserCenterActivity;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CircleImageView;
import com.library.im.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.hawk.Hawk;

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

public class GPCommentActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {

    //    @Bind(R.id.titleBar)
//    ToolBarView toolBarView;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
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
//        bean = (GPResponse.GpListBean) getIntent().getSerializableExtra(Global.KEY_OBJ);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        initCommentList();
        initDpBean();
        etComment.setOnKeyListener(onKeyListener);
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
    };

    private void initDpBean() {
        if (!TextUtils.isEmpty(gpId)) {
            loadIngShow();
            Http.queryGpById(context, gpId, new CallBack<GpSingleRespone>() {
                @Override
                public void handlerSuccess(GpSingleRespone data) {
                    loadIngDismiss();
                    initData(data);
                }

                @Override
                public void fail(int code, String message) {
                    loadIngDismiss();
                    ToastUtils.showLong(message);
                }
            });
        }
    }

    private void initData(GpSingleRespone data) {
        if (data != null) {
            this.bean = data.getBean();
            lookStatics();
            initToolBar();
            initCommentList();
        }
    }

    private void lookStatics() {
        Http.doGP(GPCommentActivity.this, bean.getGpId(), Global.TYPE_SEE, "", new CallBack<DoGPResponse>() {
            @Override
            public void handlerSuccess(DoGPResponse data) {
            }

            @Override
            public void fail(int code, String message) {
                ToastUtils.showLong(message);
            }
        });
    }

    boolean isHuifuTan = false;

    //打开软键盘
    public void openInputMethodManager() {
        isHuifuTan = true;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //关闭软键盘
    public void closeInputMethodManager() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    int clickPosition = -1;

    private void initCommentList() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(context, bean.getCommentList());
        rvComment.setAdapter(commentAdapter);

        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etComment.requestFocus();
                clickPosition = position;
                openInputMethodManager();
            }
        });

        if (TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
            headView = LayoutInflater.from(this).inflate(R.layout.layout_comment_hb_top, null);
            ImageView ivHB = (ImageView) headView.findViewById(R.id.iv_hb);
            ivHB.setOnClickListener(v -> {
                loadIngShow();
                Http.doGP(GPCommentActivity.this, bean.getGpId(), Global.TYPE_COMPETE, "", new CallBack<DoGPResponse>() {
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
            PicAdapter picAdapter = new PicAdapter(context, getPicArray(), bean.getType());
            rvPic.setAdapter(picAdapter);
        }
        TextView tvUserName = (TextView) headView.findViewById(R.id.tv_name);
        TextView tvTime = (TextView) headView.findViewById(R.id.tv_time);
        TextView tvMessage = (TextView) headView.findViewById(R.id.tv_message);
        TextView tvZan = (TextView) headView.findViewById(R.id.tv_zan);
        TextView tvSee = (TextView) headView.findViewById(R.id.tv_see);
        tvLY = (TextView) headView.findViewById(R.id.tv_liuyan);
        TextView tv_sex = (TextView) headView.findViewById(R.id.tv_sex);

        tvZan.setOnClickListener(v -> {
            loadIngShow();
            Http.doGP(GPCommentActivity.this, bean.getGpId(), Global.TYPE_GRANT, "", new CallBack<DoGPResponse>() {
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
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserCenterActivity.class);
                intent.putExtra("user_id", bean.getUserId());
                intent.putExtra("from", "GPCommentActivity_guapi");
                intent.putExtra("destName", bean.getUserName());
                intent.putExtra(EaseConstant.EXTRA_USER_ID, bean.getUser_hid());
                context.startActivity(intent);
            }
        });

        tv_sex.setText(bean.getAge());
        tvUserName.setText(bean.getUserName());
//        tvTime.setText(bean.getCreateTime());
        tvMessage.setText(bean.getDesc());
        tvZan.setText(bean.getGrantTotal());
        tvSee.setText(bean.getView_total());
        tvLY.setText(bean.getCommentTotal());

        String time = bean.getCreateTime();
        if (!TextUtils.isEmpty(time)) {
            if (time.contains(".0")) {
                time = time.substring(0, time.indexOf("."));
            }
            tvTime.setText(com.guapi.tool.Utils.getTimeStr(time));
        }
        int sex = bean.getSex();
        Drawable nav_up = null;
        if (sex == 1) {
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

    private List<PicEntity> getPicArray() {
        List<PicEntity> array = new ArrayList<>();
        if (TextUtils.equals(bean.getType(), Global.TYPE_PIC)) {
            addPic(array, bean.getPicFile1Url());
            addPic(array, bean.getPicFile2Url());
            addPic(array, bean.getPicFile3Url());
            addPic(array, bean.getPicFile4Url());
            addPic(array, bean.getPicFile5Url());
            addPic(array, bean.getPicFile6Url());
            addPic(array, bean.getPicFile7Url());
            addPic(array, bean.getPicFile8Url());
            addPic(array, bean.getPicFile9Url());
        } else {
            AddPic(array, bean);
        }
        return array;
    }

    private void AddPic(List<PicEntity> array, GPResponse.GpListBean bean) {
        if (!TextUtils.isEmpty(bean.getVideo_pic())) {
            array.add(new PicEntity(bean.getVideo_pic(), bean.getVideo_url()));
        }
    }

    private void addPic(List<PicEntity> array, String pic) {
        if (!TextUtils.isEmpty(pic)) {
            array.add(new PicEntity(pic, ""));
        }
    }

    private void initToolBar() {
        if (TextUtils.equals(bean.getType(), Global.TYPE_PIC)) {
//            toolBarView.setTitleText("照片瓜皮");
            tvTitle.setText("照片瓜皮");
        } else if (TextUtils.equals(bean.getType(), Global.TYPE_VIDEO)) {
//            toolBarView.setTitleText("视频瓜皮");
            tvTitle.setText("视频瓜皮");
        } else if (TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
//            toolBarView.setTitleText("红包瓜皮");
            tvTitle.setText("红包瓜皮");
        }
//        toolBarView.setBackPressed(this);
//        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

    }

    @OnClick({R.id.iv_face, R.id.iv_send, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_face:
                break;
            case R.id.iv_send:
                if (TextUtils.isEmpty(etComment.getText())) {
                    return;
                }
                String message = "";
                if (isHuifuTan) {
                    LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
                    message = loginResponse.getUser().getNickname() + "回复:" + commentAdapter.getData().get(clickPosition).getCommentUserName() + etComment.getText().toString();
                } else {
                    message = etComment.getText().toString();
                }
                doGP(message);
                break;
        }
    }

    public void doGP(String message) {
        loadIngShow();
        Http.doGP(this, bean.getGpId(), Global.TYPE_COMMENT, message, new CallBack<DoGPResponse>() {
            @Override
            public void handlerSuccess(DoGPResponse data) {
                isHuifuTan = false;
                loadIngDismiss();
                etComment.setText("");
                LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
                GPResponse.GpListBean.CommentListBean bean = new GPResponse.GpListBean.CommentListBean();
                bean.setCommentInfo(message);
                bean.setCommentTime(TimeUtils.getNowString());
                bean.setCommentUserId(loginResponse.getUser().getUid());
                bean.setCommentUserName(loginResponse.getUser().getNickname());
                bean.setCommentUserImagUrl(loginResponse.getUser().getAvatarUrl());
                String sex = loginResponse.getUser().getSex();
                String age = loginResponse.getUser().getAge();

                int ss = 0;
                int aa = 0;
                if (!TextUtils.isEmpty(sex)) {
                    ss = Integer.parseInt(sex);
                }
                if (!TextUtils.isEmpty(age)) {
                    aa = Integer.parseInt(age);
                }
                bean.setSex(ss);
                Log.e("LongGPCOMMENT", aa + "__" + ss);
                bean.setAge(aa);
                commentAdapter.addData(bean);
                tvCommentCount.setText(StringFormat.formatForRes(R.string.comment_count, commentAdapter.getData().size()));
                tvLY.setText(String.valueOf(commentAdapter.getData().size()));
            }

            @Override
            public void fail(int code, String message) {
                loadIngDismiss();
                ToastUtils.showLong(message);
                if (code == Constants.NET_CODE_NEED_LOGIN) {
                    IntentUtil.startActivity(GPCommentActivity.this, LoginActivity.class, false);
                }
            }
        });
    }
}
