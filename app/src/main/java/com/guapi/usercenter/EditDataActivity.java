package com.guapi.usercenter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.common.utils.SdCardUtil;
import com.ewuapp.framework.common.utils.Uri2Patch;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.ewuapp.framework.view.widget.alertview.AlertView;
import com.ewuapp.framework.view.widget.alertview.OnItemClickListener;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.model.response.LoginResponse;
import com.guapi.model.response.UserGetCountResponse;
import com.guapi.tool.DateUtil;
import com.guapi.tool.PreferenceKey;
import com.library.im.controller.HxHelper;
import com.library.im.utils.PreferenceManager;
import com.nanchen.compresshelper.CompressHelper;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/6/23.
 */

public class EditDataActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.et_nick_name)
    EditText etNickName;
    @Bind(R.id.et_sign)
    EditText etSign;
    @Bind(R.id.tv_label)
    TextView tvLabel;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.iv_4)
    ImageView iv4;
    @Bind(R.id.iv_5)
    ImageView iv5;
    @Bind(R.id.iv_6)
    ImageView iv6;

    private TimePickerView pvTime;
    UserGetCountResponse.DataBean dataBean;
    public static final int REQUEST_CODE_CAMERA = 1001;
    public static final int REQUEST_CODE_PHOTO = 2001;
    public static final int REQUEST_CODE_PHOTO_DEAL = 3001;

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
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            dataBean = (UserGetCountResponse.DataBean) bundle.getSerializable("DataBean");
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("编辑资料");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        toolBarView.setRightText("保存");
        toolBarView.setTextColor(ToolBarView.TEXT_RIGHT, Color.parseColor("#ffffff"));
        toolBarView.setOnRightClickListener(new ToolBarView.OnBarRightClickListener() {
            @Override
            public void onRightClick(View v) {
                editUser(etNickName.getText().toString(), tvBirthday.getText().toString(), etSign.getText().toString(), tvLabel.getText().toString(), tvSex.getText().toString());
            }
        });
    }

    private void editUser(String nickName, String birthday, String sign, String label, String sex) {
        int age = 0;
        if (!CheckUtil.isNull(birthday)) {
            String[] bd = birthday.split("-");
            String nowYear = DateUtil.getNowTimeYear();
            age = Integer.valueOf(nowYear) - Integer.valueOf(bd[0]);
        }
        File[] files = null;
        files = new File[6];
        List<String> photoPathList = new ArrayList<>();
        List<Integer> imgList = new ArrayList<>();
        if (!CheckUtil.isNull(path1)) {
            File file1 = new File(path1);
            files[0] = file1;
            imgList.add(0);
            photoPathList.add(path1);
        }
        if (!CheckUtil.isNull(path2)) {
            File file2 = new File(path2);
            files[1] = file2;
            imgList.add(1);
            photoPathList.add(path2);
        }
        if (!CheckUtil.isNull(path3)) {
            File file3 = new File(path3);
            files[2] = file3;
            imgList.add(2);
            photoPathList.add(path3);
        }
        if (!CheckUtil.isNull(path4)) {
            File file4 = new File(path4);
            files[3] = file4;
            imgList.add(3);
            photoPathList.add(path4);
        }
        if (!CheckUtil.isNull(path5)) {
            File file5 = new File(path5);
            files[4] = file5;
            imgList.add(4);
            photoPathList.add(path5);
        }
        if (!CheckUtil.isNull(path6)) {
            File file6 = new File(path6);
            files[5] = file6;
            imgList.add(5);
            photoPathList.add(path6);
        }
        int sexInt = 0;
        if (sex.equals("男")) {
            sexInt = 0;
        } else if (sex.equals("女")) {
            sexInt = 1;
        }
        loadIngShow();
        if (photoPathList.size() > 0) {//压缩图片
            File[] newFils = new File[6];
            for (int i = 0; i < photoPathList.size(); i++) {
                File newFile = CompressHelper.getDefault(context).compressToFile(files[imgList.get(i)]);
                newFils[imgList.get(i)] = newFile;
                if (i == photoPathList.size() - 1) {
                    int finalSexInt = sexInt;
                    int finalAge = age;
                    addDisposable(Http.update(context, nickName, sexInt, "", "", sign, age, label, birthday, new CallBack<Result>() {
                        @Override
                        public void handlerSuccess(Result data) {
                            Hawk.put(PreferenceKey.AVATAR, dataBean.getAvatarUrl());
                            HxHelper.avatar = Hawk.get(PreferenceKey.AVATAR, "");
                            PreferenceManager.getInstance().setUserAvatar(dataBean.getAvatarUrl());
                            LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
                            LoginResponse.UserBean userBean = loginResponse.getUser();
                            userBean.setAvatarUrl(dataBean.getAvatarUrl());
                            userBean.setPic_file1_url(dataBean.getPic_file1_url());
                            userBean.setPic_file2_url(dataBean.getPic_file2_url());
                            userBean.setPic_file3_url(dataBean.getPic_file3_url());
                            userBean.setPic_file4_url(dataBean.getPic_file4_url());
                            userBean.setPic_file5_url(dataBean.getPic_file5_url());
                            userBean.setPic_file6_url(dataBean.getPic_file6_url());
                            userBean.setSex(finalSexInt + "");
                            userBean.setAge(finalAge + "");
                            userBean.setNickname(nickName);
                            loginResponse.setUser(userBean);
                            Hawk.put(PreferenceKey.LoginResponse, loginResponse);
                            GlideUtil.clear();
                            loadIngDismiss();
                            finish();
                            showMessage("完善个人资料成功");
                        }

                        @Override
                        public void fail(int code, String message) {
                            loadIngDismiss();
                            showMessage(message);
                        }
                    }, imgList, newFils));
                }
            }
        } else {
            addDisposable(Http.update(context, nickName, sexInt, "", "", sign, age, label, birthday, new CallBack<Result>() {
                @Override
                public void handlerSuccess(Result data) {
                    GlideUtil.clear();
                    loadIngDismiss();
                    finish();
                    showMessage("完善个人资料成功");
                }

                @Override
                public void fail(int code, String message) {
                    loadIngDismiss();
                    showMessage(message);
                }
            }, imgList, files));
        }
    }

    private void showSexChoose() {
        new AlertView("性别", null, "取消", null,
                new String[]{"男", "女"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    tvSex.setText("男");
                } else if (position == 1) {
                    tvSex.setText("女");
                }
            }
        }).show();
    }


    private void showPickUp(int myPicPosition) {
        new AlertView("上传照片", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                picturePositon = myPicPosition;
                if (position == 0) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        insertDummyContactWrapper();
                    } else {
                        openCamera();
                    }
                } else if (position == 1) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        insertPhoto();
                    } else {
                        pickUpPhoto();
                    }
                }
            }
        }).show();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_edit_data;
    }

    @OnClick({R.id.rl_birthday, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5, R.id.iv_6, R.id.ll_label, R.id.ll_sex})
    public void onClcik(View view) {
        switch (view.getId()) {
            case R.id.rl_birthday:
                showTimeDialog();
                break;
            case R.id.iv_1:
                showPickUp(1);
                break;
            case R.id.iv_2:
                showPickUp(2);
                break;
            case R.id.iv_3:
                showPickUp(3);
                break;
            case R.id.iv_4:
                showPickUp(4);
                break;
            case R.id.iv_5:
                showPickUp(5);
                break;
            case R.id.iv_6:
                showPickUp(6);
                break;
            case R.id.ll_label:
                startForResult(null, GET_LABEL, ChooseLabelActivity.class);
                break;
            case R.id.ll_sex:
                showSexChoose();
                break;
        }
    }

    protected static final int GET_LABEL = 101;
    int picturePositon = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        etNickName.setText(CheckUtil.isNull(dataBean.getNickname()) ? "" : dataBean.getNickname());
        etSign.setText(CheckUtil.isNull(dataBean.getNote()) ? "" : dataBean.getNote());
        tvLabel.setText(CheckUtil.isNull(dataBean.getLableInfor()) ? "" : dataBean.getLableInfor());
        tvBirthday.setText(CheckUtil.isNull(dataBean.getBirthday()) ? "" : dataBean.getBirthday());
        GlideUtil.loadPicture(dataBean.getPic_file1_url(), iv1, R.mipmap.tjzpan);
        GlideUtil.loadPicture(dataBean.getPic_file2_url(), iv2, R.mipmap.tjzpan);
        GlideUtil.loadPicture(dataBean.getPic_file3_url(), iv3, R.mipmap.tjzpan);
        GlideUtil.loadPicture(dataBean.getPic_file4_url(), iv4, R.mipmap.tjzpan);
        GlideUtil.loadPicture(dataBean.getPic_file5_url(), iv5, R.mipmap.tjzpan);
        GlideUtil.loadPicture(dataBean.getPic_file6_url(), iv6, R.mipmap.tjzpan);
        if (!CheckUtil.isNull(dataBean.getSex())) {
            if (dataBean.getSex().equals("0")) {
                tvSex.setText("男");
            } else if (dataBean.getSex().equals("1")) {
                tvSex.setText("女");
            }
        }
    }

    /**
     * 时间选择
     */
    private void showTimeDialog() {
        if (pvTime == null) {
            pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
            pvTime.setTime(new Date());
            pvTime.setRange(1990, Integer.valueOf(DateUtil.getNowTimeYear()));
            pvTime.setCyclic(false);
            pvTime.setCancelable(true);
        }
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                String nowYear, nowMonth, nowDay;
                nowYear = DateUtil.getNowTimeYear();
                nowMonth = DateUtil.getNowTimeOnlyMonth();
                nowDay = DateUtil.getNowTimeOnlDay();
                String chooseDat = getTime(date);
                String[] splits = chooseDat.split("-");
                String chooseYear, chooseMonth, chooseDay;
                chooseYear = splits[0];
                chooseMonth = splits[1];
                chooseDay = splits[2];
                if (Integer.valueOf((chooseYear + "" + chooseMonth + "" + chooseDay)) > Integer.valueOf((nowYear + "" + nowMonth + "" + nowDay))) {
                    showMessage("请正确选择出生日期");
                } else {
                    tvBirthday.setText(getTime(date));
                }
            }
        });
        if (tvBirthday != null) {
            pvTime.show();
        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 动态权限检查
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//没有授权
            if (hasWriteContactsPermission == PackageManager.PERMISSION_DENIED) {
                showMessageOKCancel("请在应用权限中允许\n\t打开摄像头权限",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请该权限时引导用户跳转到Setting中自己去开启权限开关
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            }
                        });
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            openCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        String picPath = "";
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case GET_LABEL:
                tvLabel.setText(data.getStringExtra("LABEL"));
                break;
            case REQUEST_CODE_CAMERA:
                uri = Uri.parse("file:///" + getPath());
                picPath = uri.getPath();
                setPath(picPath);
                Log.e("LONG_PATH", picPath);
                setIvBitmap(picPath);
                break;
            case REQUEST_CODE_PHOTO:
                uri = Uri.parse("file:///" + Uri2Patch.getPath(this, data.getData()));
                picPath = uri.getPath();
                setPath(picPath);
                Log.e("LONG_PATH", picPath);
                setIvBitmap(picPath);
                break;
        }
    }

    public void setIvBitmap(String path) {
        switch (picturePositon) {
            case 1:
                GlideUtil.loadPicture(path, iv1);
                break;
            case 2:
                GlideUtil.loadPicture(path, iv2);
                break;
            case 3:
                GlideUtil.loadPicture(path, iv3);
                break;
            case 4:
                GlideUtil.loadPicture(path, iv4);
                break;
            case 5:
                GlideUtil.loadPicture(path, iv5);
                break;
            case 6:
                GlideUtil.loadPicture(path, iv6);
                break;
        }
    }

    /**
     * 打开照相
     */
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        String path = SdCardUtil.getTempCamera();
        setPath(path);
        Log.e("Long_open_camera_path", path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file:///" + path));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void setPath(String mpath) {
        switch (picturePositon) {
            case 1:
                path1 = mpath;
                break;
            case 2:
                path2 = mpath;
                break;
            case 3:
                path3 = mpath;
                break;
            case 4:
                path4 = mpath;
                break;
            case 5:
                path5 = mpath;
                break;
            case 6:
                path6 = mpath;
                break;
        }
    }

    public String getPath() {
        switch (picturePositon) {
            case 1:
                return path1;
            case 2:
                return path2;
            case 3:
                return path3;
            case 4:
                return path4;
            case 5:
                return path5;
            case 6:
                return path6;
            default:
                return "";
        }
    }

    String path1 = "", path2 = "", path3 = "", path4 = "", path5 = "", path6 = "";

    /**
     * 打开照片选择
     */
    public void pickUpPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    /**
     * 动态权限检查
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void insertPhoto() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//没有授权
            if (hasWriteContactsPermission == PackageManager.PERMISSION_DENIED) {
                showMessageOKCancel("请在应用权限中允许\n\t打开查看相册权限",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请该权限时引导用户跳转到Setting中自己去开启权限开关
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            }
                        });
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_PHOTO);
            }
        } else {
            pickUpPhoto();
        }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//权限请求码
    final private int REQUEST_CODE_ASK_PERMISSIONS_PHOTO = 122;//权限请求码

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//权限申请成功
                    openCamera();
                }
                break;
            case REQUEST_CODE_ASK_PERMISSIONS_PHOTO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//权限申请成功
                    pickUpPhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("拒绝", null)
                .create()
                .show();
    }

}
