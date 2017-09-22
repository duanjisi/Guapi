package com.guapi.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.guapi.R;
import com.guapi.tool.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.guapi.tool.Global.TYPE_PIC;
import static com.guapi.tool.Global.TYPE_VIDEO;

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
 * @since 2017/6/27 0027
 */

public class ChooseTypeActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;

    @Bind(R.id.tv_hb)
    TextView tvHb;
    @Bind(R.id.tv_pic)
    TextView tvPic;
    @Bind(R.id.tv_video)
    TextView tvVideo;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

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
        return R.layout.activity_choose_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }


    @OnClick({R.id.tv_hb, R.id.tv_pic, R.id.tv_video, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hb:
//                gotoCamera(TYPE_HB);
                showToast("功能正在开发中", true);
                break;
            case R.id.tv_pic:
                gotoCamera(TYPE_PIC);
                break;
            case R.id.tv_video:
                gotoCamera(TYPE_VIDEO);
                break;
            case R.id.tv_cancel:
                finish();
                overridePendingTransition(0, R.anim.slide_out_to_bottom);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }

    public void gotoCamera(String type) {
        final String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        final List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Global.KEY_STR, type);
//            IntentUtil.startActivityWithBundle(this, CameraActivity.class, bundle, true);
            IntentUtil.startActivityWithBundle(this, CameraHideActivity.class, bundle, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
//            IntentUtil.startActivity(this, CameraActivity.class, true);
            IntentUtil.startActivity(this, CameraHideActivity.class, true);
        }
    }

}