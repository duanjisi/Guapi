package com.guapi.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.guapi.BaseApp;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.main.adapter.OnSubmitClick;
import com.guapi.model.ActionEntity;
import com.guapi.model.PointEntity;
import com.guapi.tool.Constants;
import com.guapi.tool.Global;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import me.shaohui.advancedluban.Luban;
import timber.log.Timber;

import static com.guapi.tool.Global.ACTIVITY_REQUEST_VIDEO_PATH;
import static com.guapi.tool.Global.PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.guapi.tool.Global.TYPE_HB;
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
 * @since 2017/7/7 0007
 */

public class CameraActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> implements
        OnSubmitClick {

    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.iv_point)
    ImageView ivPoint;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.camera)
    CameraView camera;
    @Bind(R.id.iv_focus)
    ImageView ivFocus;

//    private AMapLocationClient mlocationClient;
//    private AMapLocationClientOption mLocationOption;

    private String type = TYPE_HB;

    private List<String> picArray = new ArrayList<>(); // 藏图片变量

    private CameraRunnable cameraRunnable;

    private String pointKey; // 线索图片路径
    private String videoKey; // 视频路径
    private Map<String, File> uploadFileArray = new HashMap<>(); // 第一张为线索图片


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
        return R.layout.activity_camera;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        type = getIntent().getStringExtra(Global.KEY_STR);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        showButton(false);
        AMapLocation aMapLocation = BaseApp.getInstance().getUserLocation();
        if (aMapLocation != null) {
            lat = aMapLocation.getLatitude();
            lng = aMapLocation.getLongitude();
            address = aMapLocation.getAddress();
            tvLocation.setText(address);
        }

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddressSelectActivity.class);
                startActivityForResult(intent, 101);
            }
        });
        if (!TextUtils.equals(type, TYPE_HB)) {
            picArray.add("head");
        }
//        initMap();
        cameraRunnable = new CameraRunnable(camera, type);
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            if (action.equals(Constants.Action.CHANGE_ADDRESS)) {
                PointEntity point = (PointEntity) event.getData();
                if (point != null) {
                    lat = point.getLat();
                    lng = point.getLng();
                    address = point.getAddress();
                    tvLocation.setText(address);
                }
            }
        }
    }

    private void showButton(boolean shown) {
        tvTip.setVisibility(shown ? View.GONE : View.VISIBLE);
        btnCancel.setVisibility(shown ? View.VISIBLE : View.GONE);
        btnSubmit.setVisibility(shown ? View.VISIBLE : View.GONE);
        ivPoint.setVisibility(shown ? View.VISIBLE : View.GONE);
        if (shown && !uploadFileArray.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(pointKey);
            bitmap = centerSquareScaleBitmap(bitmap, 200);
            ivPoint.setImageBitmap(bitmap);
//            Glide.with(this).load(uploadFileArray.get(pointKey)).into(ivPoint);
        }
    }

    boolean hasInitCamera = false;

    @Override
    protected void onResume() {
        super.onResume();
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                camera.start();
                if (hasInitCamera) {
                    return;
                }
                hasInitCamera = true;
                camera.setJpegQuality(20);
                camera.setCropOutput(true);
                camera.setFlash(CameraKit.Constants.FLASH_AUTO);
                camera.setVideoQuality(CameraKit.Constants.VIDEO_QUALITY_LOWEST);
                camera.setCameraListener(new CameraListener() {
                    @Override
                    public void onPictureTaken(byte[] jpeg) {
                        super.onPictureTaken(jpeg);
                        // Create a bitmap
                        String filePath = getCacheDir().getAbsolutePath() + "/pictures" + File.separator + System.currentTimeMillis() + ".jpg";
                        if (FileUtils.createOrExistsFile(filePath)) {
                            File file = new File(filePath);
                            if (!FileIOUtils.writeFileFromBytesByStream(file, jpeg)) {
                                ToastUtils.showLong("无法将图片存储到手机中");
                                finish();
                            } else {
                                Timber.d("图片路径:%s", filePath);
                                uploadFileArray.put(filePath, file);
                                me.shaohui.advancedluban.Luban.compress(CameraActivity.this, file)
                                        .putGear(Luban.THIRD_GEAR)
                                        .launch(new me.shaohui.advancedluban.OnCompressListener() {
                                            @Override
                                            public void onStart() {
                                                loadIngShow();
                                            }

                                            @Override
                                            public void onSuccess(File file) {
                                                loadIngDismiss();
                                                pointKey = filePath;
                                                uploadFileArray.put(filePath, file);
                                                float fileLength = file.length() / (1024f);
                                                Timber.d("当前压缩图片新路径:%1$s, 图片大小:%2$s", file.getAbsolutePath(), fileLength + "k");
                                                showButton(true);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                loadIngDismiss();
                                                ToastUtils.showLong("图片处理出错");
                                                finish();
                                            }
                                        });
                            }
                        } else {
                            ToastUtils.showLong("无法创建文件");
                            finish();
                        }
                    }
                });
                capture();
            }
        }, 2000);
    }

    private void capture() {
        showButton(false);
        YoYo.with(Techniques.RotateOut).duration(1000).repeat(10).playOn(ivFocus);
        getMainHandler().postDelayed(cameraRunnable, 3000);
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        if (TextUtils.equals(type, TYPE_HB)) {
            toolBarView.setTitleText("藏红包");
        } else if (TextUtils.equals(type, TYPE_PIC)) {
            toolBarView.setTitleText("藏照片");
        } else if (TextUtils.equals(type, TYPE_VIDEO)) {
            toolBarView.setTitleText("藏视频");
        }
        toolBarView.setBackPressed(this);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setDrawable(ToolBarView.TEXT_RIGHT, R.mipmap.ic_flash);
        toolBarView.setOnRightClickListener(new ToolBarView.OnBarRightClickListener() {
            @Override
            public void onRightClick(View v) {
//                camera.toggleFlash();
                camera.setFlash(CameraKit.Constants.FLASH_ON);
            }
        });
    }


    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                capture();
                break;
            case R.id.btn_submit:
                if (TextUtils.equals(type, TYPE_HB)) {
                    showHBDialog();
                } else {
                    showPicDialog();
                }
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMainHandler().removeCallbacks(cameraRunnable);
    }

    private void showHBDialog() {
        MoneyDataDialog dataDialog = new MoneyDataDialog(this);
        dataDialog.setOnSubmitClick(this);
        dataDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showButton(false);
            }
        });
        dataDialog.show();
    }

    private PicDataDialog dataDialog = null;

    private void showPicDialog() {
        dataDialog = new PicDataDialog(this, picArray, type);
        dataDialog.setOnSubmitClick(this);
        dataDialog.setOnPictureAction(new PicDataDialog.OnPictureAction() {
            @Override
            public void onRequestAddNewPicture() {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // 提示用户如果想要正常使用，要手动去设置中授权。
                        ToastUtils.showLong("请在 设置-应用管理 中开启此应用的储存授权。");
                    } else {
                        ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    Timber.i("不需要授权 ");
                    // 进行正常操作
//                    dataDialog.dismiss();
                    if (TextUtils.equals(type, TYPE_PIC)) {
                        initImagePicker();
                    } else {
                        IntentUtil.startActivityForResult(CameraActivity.this, VideoRecordActivity.class, ACTIVITY_REQUEST_VIDEO_PATH, false);
                    }
                }
            }

            @Override
            public void onRequestRemovePicture(String key) {
                Timber.d("移除的图片:%s", key);
                uploadFileArray.remove(key);
            }
        });
        dataDialog.show();
    }

    private void showSuccessDialog(String type) {
        MoneySuccessDialog dialog = new MoneySuccessDialog(this, uploadFileArray.get(pointKey), type);
        dialog.setOnCancelClick(new MoneySuccessDialog.OnCancelClick() {
            @Override
            public void onCancel() {
//                showButton(false);
                //去地图刷新界面
                EventBus.getDefault().post(new ActionEntity("com.guapi.refresh.map"));
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onSubmitHB(Dialog dialog, String money, String count, String showType, String message) {
//        AMapLocation location = BaseApp.getInstance().getUserLocation();
//        if (location == null) {
//            ToastUtils.showLong("无法定位，请退出重试");
//            return;
//        }
        loadIngShow();
        File file = uploadFileArray.get(pointKey);
        Http.createHB(CameraActivity.this, TYPE_HB, money, count, showType, message,
                lat, lng, address,
                new CallBack<Result>() {
                    @Override
                    public void handlerSuccess(Result data) {
                        loadIngDismiss();
                        dialog.dismiss();
                        showSuccessDialog(type);
                    }

                    @Override
                    public void fail(int code, String message) {
                        loadIngDismiss();
                        ToastUtils.showLong("上传失败啦，原因" + message);
                        dialog.dismiss();
                    }
                }, file);
    }

    @Override
    public void onSubmitPic(Dialog dialog, String message) {
        if (TextUtils.equals(type, TYPE_PIC)) {
            uploadPicture(dialog, message);
        } else if (TextUtils.equals(type, TYPE_VIDEO)) {
            uploadVideo(dialog, message);
        }
    }

    private double lat, lng;
    private String address;

    private void uploadPicture(Dialog dialog, String message) {
        AMapLocation location = BaseApp.getInstance().getUserLocation();
        if (location == null) {
            ToastUtils.showLong("无法定位，请退出重试");
            return;
        }
        dialog.dismiss();

        File[] files = null;
        Timber.d("需要上传的图片总共有:%s张", picArray.size() - 1);
        if (!uploadFileArray.isEmpty() && (uploadFileArray.size() == (picArray.size() - 1))) {
            List<File> newFiles = new ArrayList<>();
            for (Iterator iterator = uploadFileArray.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                File file = (File) entry.getValue();
                float fileLength = file.length() / (1024f);
                Timber.d("需要上传的压缩图片大小:%s", fileLength + "k");
                newFiles.add(file);
            }
            files = (File[]) newFiles.toArray();
        } else if (picArray.size() > 1) {
            files = new File[picArray.size() - 1];
            for (int i = 0; i < picArray.size() - 1; i++) {
                files[i] = new File(picArray.get(i + 1));
                float fileLength = files[i].length() / (1024f);
                Timber.d("需要上传的原始图片大小:%s", fileLength + "k");
            }
        }

        File file = uploadFileArray.get(pointKey);
        Timber.d("需要上传的图片路径:%s", Arrays.asList(files));

        loadIngShow();
        Http.createPic(CameraActivity.this, TYPE_PIC, message,
                location.getLatitude(), location.getLongitude(), location.getStreet(),
                new CallBack<Result>() {
                    @Override
                    public void handlerSuccess(Result data) {
                        loadIngDismiss();
                        dialog.dismiss();
                        showSuccessDialog(type);
                    }

                    @Override
                    public void fail(int code, String message) {
                        loadIngDismiss();
                        ToastUtils.showLong("上传失败啦，原因" + message);
                        dialog.dismiss();
                    }
                }, file, files);
    }

    private void uploadVideo(Dialog dialog, String message) {
//        AMapLocation location = BaseApp.getInstance().getUserLocation();
//        if (location == null) {
//            ToastUtils.showLong("无法定位，请退出重试");
//            return;
//        }
        dialog.dismiss();

        File pointFile = new File(pointKey);
        File videoFile = new File(videoKey);

        loadIngShow();
//        Http.createVideo(CameraActivity.this, TYPE_VIDEO, message,
//                lat, lng, address,
//                new CallBack<Result>() {
//                    @Override
//                    public void handlerSuccess(Result data) {
//                        loadIngDismiss();
//                        dialog.dismiss();
//                        showSuccessDialog(type);
//                    }
//
//                    @Override
//                    public void fail(int code, String message) {
//                        loadIngDismiss();
//                        ToastUtils.showLong("上传失败啦，原因" + message);
//                        dialog.dismiss();
//                    }
//                }, videoFile, pointFile);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.i("同意授权");
                // 进行正常操作。
                initImagePicker();
            } else {
                Timber.i("拒绝授权");
                ToastUtils.showLong("应用需要授权读取存储卡权限");
            }
        }
    }

    int compressPicSize = 0;

    private void initImagePicker() {
        compressPicSize = 0;
        picArray.remove(0);
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Timber.i("onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Timber.i("onSuccess: 返回数据");
                // 直接赋值给传入的list
                picArray.clear();
                picArray.add("head");
                picArray.addAll(photoList);
                Timber.d("返回图片集合： %s", picArray);
                loadIngShow();
                for (int i = 0; i < photoList.size(); i++) {
                    final String originFilePath = photoList.get(i);
                    me.shaohui.advancedluban.Luban.compress(CameraActivity.this, new File(originFilePath))
                            .putGear(Luban.THIRD_GEAR)
                            .launch(new CompressImage(originFilePath, photoList.size()));
                }
            }

            @Override
            public void onCancel() {
                Timber.i("onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Timber.i("onFinish: 结束");
            }

            @Override
            public void onError() {
                Timber.e("onError: 出错");
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder().imageLoader(new ImageLoader() {
            @Override
            public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
                Glide.with(activity).load(path).into(galleryImageView);
            }

            @Override
            public void clearMemoryCache() {

            }
        })    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(picArray)                         // 记录已选的图片
                // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .provider("com.guapi.fileprovider")   // provider(必填)
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath(getCacheDir().getAbsolutePath() + "/pictures")          // 图片存放路径
                .build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == ACTIVITY_REQUEST_VIDEO_PATH) {
            videoKey = data.getStringExtra(Global.KEY_VIDEO_PATH);
            picArray.clear();
            picArray.add("head");
            picArray.add(videoKey);
            showPicDialog();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            String address = aMapLocation.getPoiName();
//            if (!TextUtils.isEmpty(address)) {
//                tvLocation.setText(address + " >");
//            }
//        }
//    }

    private static class CameraRunnable implements Runnable {

        private String type;
        private WeakReference<CameraView> cameraViewWeakReference;

        CameraRunnable(CameraView view, String type) {
            this.type = type;
            cameraViewWeakReference = new WeakReference<CameraView>(view);
        }

        @Override
        public void run() {
            CameraView cameraView = cameraViewWeakReference.get();
            if (cameraView == null) {
                return;
            } else {
                cameraView.captureImage();
            }
        }
    }

    private class CompressImage implements me.shaohui.advancedluban.OnCompressListener {

        private String key;
        private int size;

        CompressImage(String key, int size) {
            this.key = key;
            this.size = size;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(File file) {
            compressPicSize++;
            uploadFileArray.put(key, file);
            float fileLength = file.length() / (1024 * 1024f);
            Timber.d("当前压缩图片新路径:%1$s, 图片大小:%2$s", file.getAbsolutePath(), fileLength + "M");
            Timber.d("已压缩图片:%s张", compressPicSize);

            if (compressPicSize == size) {
                loadIngDismiss();
                showPicDialog();
            }
        }

        @Override
        public void onError(Throwable e) {
            loadIngDismiss();
            ToastUtils.showLong("图片处理出错");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (hasInitCamera) {
            super.onBackPressed();
        }
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }
//    private void initMap() {
//        if (mlocationClient == null) {
//            //初始化定位
//            mlocationClient = new AMapLocationClient(this);
//            //初始化定位参数
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位回调监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            mLocationOption.setInterval(30000);
//            mlocationClient.setLocationOption(mLocationOption);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mlocationClient.startLocation();//启动定位
//        }
//    }
}
