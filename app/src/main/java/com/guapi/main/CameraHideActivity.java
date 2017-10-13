package com.guapi.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.ToastUtils;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.BaseApp;
import com.guapi.Constants;
import com.guapi.R;
import com.guapi.http.Http;
import com.guapi.main.adapter.OnSubmitClick;
import com.guapi.model.ActionEntity;
import com.guapi.model.ImageTool;
import com.guapi.tool.FileUtils;
import com.guapi.tool.Global;
import com.guapi.tool.Utils;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CameraManager;
import com.guapi.widget.scan.CameraPreview;
import com.guapi.widget.scan.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import me.shaohui.advancedluban.Luban;
import timber.log.Timber;

import static com.guapi.tool.Global.PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.guapi.tool.Global.TYPE_HB;
import static com.guapi.tool.Global.TYPE_PIC;
import static com.guapi.tool.Global.TYPE_VIDEO;

/**
 * Created by Johnny on 2017-09-07.
 */
public class CameraHideActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> implements
        OnSubmitClick,
        SensorEventListener {

    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    //    @Bind(R.id.iv_back)
//    ImageView ivBack;
//    @Bind(R.id.iv_flash)
//    ImageView ivFlash;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
    @Bind(R.id.iv_point)
    CircleImageView ivPoint;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.rl_preciew)
    RelativeLayout rl_preciew;
    @Bind(R.id.iv_focus)
    ImageView ivFocus;

    private String type = TYPE_HB;
    private String savePath;
    private File imgFile;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private CameraPreview mPreview;
    private Camera mCamera;
    private CameraManager mCameraManager;
    private Camera.Parameters parameters;
    private boolean previewing = true, isTakePic = false;
    private Calendar mCalendar;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_STATIC = 1;
    public static final int STATUS_MOVE = 2;
    private int STATUE = STATUS_NONE;
    private int mX, mY, mZ;
    private long lastStaticStamp = 0;
    boolean isFocusing = false;
    boolean canFocusIn = false;  //内部是否能够对焦控制机制
    boolean canFocus = false;
    public static final int DELEY_DURATION = 2000;
    private Bitmap bitmapImg;
    private ImageLoader imageLoader;
    private boolean isJump = false, isHideOk = true;
    private int sceenH, sceenW;

    private double lat, lng;
    private String address;

    private List<String> picArray = new ArrayList<>(); // 藏图片变量

    private String pointKey; // 线索图片路径
    private String videoKey; // 视频路径
    private Map<String, File> uploadFileArray = new HashMap<>(); // 第一张为线索图片
    private AnimationDrawable animationDrawable;
    private boolean isLighOn = false;
    private int edgeLength = 0;
    private int orientations = 0;
    private OrientationEventListener mOrientationListener;

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
        return R.layout.activity_hide_camera;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            savePath = getFilesDir().getPath();
        } else {
            savePath = Environment.getExternalStorageDirectory() + "/guapi/";
        }
        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                orientations = orientation;
                Log.v("time", "现在是横屏" + orientation);
            }
        };

        imageLoader = ImageLoaderUtils.createImageLoader(context);
        ivFocus.setImageResource(R.drawable.center_point_bg);
        animationDrawable = (AnimationDrawable) ivFocus.getDrawable();
        showButton(false);
        sceenH = (int) (Utils.getScreenHeight(context) * 0.8);
        sceenW = (int) (Utils.getScreenWidth(context) * 0.8);
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
        initViewParams();
    }


    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        type = getIntent().getStringExtra(Global.KEY_STR);
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            Log.i("info", "=====================onMessageEvent00:" + action);
            if (action.equals(Constants.Action.CLOSE_HIDE_ACTIVITY)) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                address = bundle.getString("address", "");
                lat = bundle.getDouble("lat", 0);
                lng = bundle.getDouble("lng", 0);
                if (!TextUtils.isEmpty(address)) {
                    tvLocation.setText(address);
                }
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            videoKey = data.getStringExtra("path");
            picArray.clear();
            picArray.add("head");
            picArray.add(videoKey);
            if (dataDialog != null && dataDialog.isShowing()) {
                dataDialog.refreshDailog(picArray);
            }
            getBitmapsFromVideo();
        } else if (requestCode == 103 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra("images");
            if (images != null && images.size() != 0) {
                picArray.clear();
                picArray.add("head");
                picArray.addAll(images);
                loadIngShow();
                for (int i = 0; i < images.size(); i++) {
                    final String originFilePath = images.get(i);
                    me.shaohui.advancedluban.Luban.compress(CameraHideActivity.this, new File(originFilePath))
                            .putGear(Luban.THIRD_GEAR)
                            .launch(new CompressImage(originFilePath, images.size()));
                }
            }
        }
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
//                if (mCameraManager != null) {
//                    if (!mCameraManager.isLighOn()) {
//                        mCameraManager.openLightOn();
//                    } else {
//                        mCameraManager.closeLightOff();
//                    }
//                }
                if (!isLighOn) {
                    openLightOn();
                } else {
                    closeLightOff();
                }
            }
        });
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                previewing = true;
                canFocusIn = true;
                isHideOk = true;
//                ivPoint.setVisibility(View.GONE);
                if (mCamera == null) {
                    initCamera();
                } else {
                    mCamera.startPreview();
                }
                animationDrawable.start();
                autoFocusHandler.postDelayed(doAutoFocus, 800);
                showButton(false);
//                YoYo.with(Techniques.RotateOut).duration(1000).repeat(10).playOn(ivFocus);
                break;
            case R.id.btn_submit:
                if (TextUtils.equals(type, TYPE_HB)) {
//                    showHBDialog();
                } else {
                    showPicDialog();
                }
                break;
        }
    }

    private void showButton(boolean shown) {
        tvTip.setVisibility(shown ? View.GONE : View.VISIBLE);
        btnCancel.setVisibility(shown ? View.VISIBLE : View.GONE);
        btnSubmit.setVisibility(shown ? View.VISIBLE : View.GONE);
//        ivPoint.setVisibility(shown ? View.VISIBLE : View.GONE);
//        if (shown && !uploadFileArray.isEmpty()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(pointKey);
//            bitmap = Utils.centerSquareScaleBitmap(bitmap, 200);
//            ivPoint.setImageBitmap(bitmap);
////            Glide.with(this).load(uploadFileArray.get(pointKey)).into(ivPoint);
//        }
    }

    @Override
    public void onSubmitHB(Dialog dialog, String money, String count, String showType, String message) {

    }

    @Override
    public void onSubmitPic(Dialog dialog, String message) {
        if (TextUtils.equals(type, TYPE_PIC)) {
            uploadPicture(dialog, message);
        } else if (TextUtils.equals(type, TYPE_VIDEO)) {
            uploadVideo(dialog, message);
        }
    }

    private void initViewParams() {
        edgeLength = Utils.px2dip(context, ivPoint.getLayoutParams().width);
        mSensorManager = (SensorManager) BaseApp.getInstance().getSystemService(Activity.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// T
        initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationListener != null) {//先判断下防止出现空指针异常
            mOrientationListener.enable();
        }
        animationDrawable.start();
//        getMainHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                YoYo.with(Techniques.RotateOut).duration(1000).repeat(10).playOn(ivFocus);
//            }
//        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }
        closeLightOff();
    }

    @Override
    protected void onStart() {
        super.onStart();
        canFocus = true;
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mSensor);
        canFocus = false;
    }

    public void openLightOn() {
        if (mCamera == null) {
            mCamera = mCameraManager.getCamera();
        }
        if (parameters == null) {
            parameters = mCamera.getParameters();
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        isLighOn = true;
    }

    public void closeLightOff() {
        if (mCamera == null) {
            mCamera = mCameraManager.getCamera();
        }
        if (parameters == null) {
            parameters = mCamera.getParameters();
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
//        mCamera.stopPreview();
        isLighOn = false;
    }

    private void initCamera() {
        if (mCameraManager == null) {
            mCameraManager = new CameraManager(this);
        }
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            showToast("获取摄像头出错", false);
            finish();
            return;
        } catch (RuntimeException runtimeException) {
            showToast(R.string.error_open_camera_error, false);
            finish();
            return;
        }
        if (mCamera == null) {
            mCamera = mCameraManager.getCamera();
        }
        if (parameters == null && mCamera != null) {
//            mCamera.setDisplayOrientation(90);
            parameters = mCamera.getParameters();
            // 设置预览照片的大小
            List<Camera.Size> supportedPictureSizes =
                    parameters.getSupportedPictureSizes();// 获取支持保存图片的尺寸
//            if (supportedPictureSizes != null && supportedPictureSizes.size() > 0) {
//                Camera.Size pictureSize = Utils.getPictureSize(this, supportedPictureSizes);
//                parameters.setRotation(90);
//                parameters.setPictureSize(pictureSize.width, pictureSize.height);

//                if (this.getResources().getConfiguration().orientation !=
//                        Configuration.ORIENTATION_LANDSCAPE) {
//                    parameters.set("orientation", "portrait");
//                    parameters.setRotation(90);
//                } else {
//                    parameters.set("orientation", "landscape");
//                }

//                if (Integer.parseInt(Build.VERSION.SDK) >= 8)
//                    setDisplayOrientation(mCamera, 90);
//                else {
//                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                        parameters.set("orientation", "portrait");
//                        parameters.set("rotation", 90);
//                    }
//                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                        parameters.set("orientation", "landscape");
//                        parameters.set("rotation", 90);
//                    }
//                }
//            }
//            Camera.Size optimalSize = Utils.getOptimalPreviewSize(supportedPictureSizes, sceenW, sceenH);
            Camera.Size optimalSize = Utils.getPictureSize(this, supportedPictureSizes);
            parameters.setPictureSize(optimalSize.width, optimalSize.height);
        }

        if (parameters != null && mCamera != null) {
            try {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } catch (Exception e) {

            }
        }
        if (mPreview != null) {
            mPreview = null;
        }
        mPreview = new CameraPreview(this, mCamera, mPreviewCallback, autoFocusCB);
        rl_preciew.removeAllViews();
        rl_preciew.addView(mPreview);
    }

    protected void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[]{angle});
        } catch (Exception e1) {
        }
    }

    /**
     * 获取相机内容回调方法
     */
    Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    mCamera.stopPreview();
                }
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inJustDecodeBounds = true;
//                opts.inSampleSize = ImageTool.computeSampleSize(opts, -1, sceenW * sceenH);
//                opts.inJustDecodeBounds = false;
                int roation = orientations;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                opts.inSampleSize = ImageTool.computeSampleSize(opts, -1, sceenW * sceenH);
                opts.inJustDecodeBounds = false;
//                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
//                opts.inSampleSize = ImageTool.computeSampleSize(opts, -1, sceenW * sceenH);
//                opts.inJustDecodeBounds = false;
//                bitmapImg = byteToBitmap(opts, bytes);
//                bitmapImg = Utils.ImageCrop(byteToBitmap(opts, bytes), true, edgeLength);
                Bitmap bitmap = Utils.ImageCrop(byteToBitmap(opts, bytes), true, edgeLength);
                bitmapImg = Utils.changeRoation(bitmap, roation, edgeLength);
                if (bitmapImg != null) {
//                    ivPoint.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(bytes).into(ivPoint);
//                    ivPoint.setImageBitmap(bitmapImg);
                    animationDrawable.stop();
//                    createFileWithByte(bytes);
                    saveImage(context, bitmapImg);
                    if (imgFile != null && imgFile.length() > 0) {
                        pointKey = imgFile.getAbsolutePath();
                        uploadFileArray.put(pointKey, imgFile);
                        Log.i("info", "================pointKey:" + pointKey);
                    }
                    showButton(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError error) {
                showToast("相机异常,本页面即将销毁，请重新进入", false);
                System.gc();
                autoFocusHandler.sendEmptyMessageDelayed(5, 1000);
            }
        }
    };

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing) {
                try {
                    mCamera.autoFocus(autoFocusCB);
                } catch (Exception e) {

                }
                isTakePic = true;
                previewing = false;
            }
        }
    };

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            if (mCameraManager != null)
                mCameraManager.closeDriver();
        }
    }

    Handler autoFocusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis();// 1393844912

            int second = mCalendar.get(Calendar.SECOND);// 53

            if (STATUE != STATUS_NONE) {
                int px = Math.abs(mX - x);
                int py = Math.abs(mY - y);
                int pz = Math.abs(mZ - z);
                double value = Math.sqrt(px * px + py * py + pz * pz);
                if (value > 1.4) {
                    //Log.i("onSensorChanged", "检测手机在移动");
                    STATUE = STATUS_MOVE;
                } else {
                    //Log.i("onSensorChanged", "检测手机静止");
                    //上一次状态是move，记录静态时间点
                    if (STATUE == STATUS_MOVE) {
                        lastStaticStamp = stamp;
                        canFocusIn = true;
                    }
                    if (canFocusIn) {
                        if (stamp - lastStaticStamp > DELEY_DURATION) {
                            //移动后静止一段时间，可以发生对焦行为
                            if (!isFocusing) {
                                canFocusIn = false;
                                if (!previewing && isTakePic && isHideOk) {
                                    try {
                                        mCamera.takePicture(null, null, mPictureCallback);
                                        isTakePic = false;
                                        ///对焦拍完照


                                    } catch (Exception e) {
                                        //mCamera.startPreview();
                                        canFocusIn = true;
                                        showToast("对焦失败，请移动手机重试下", false);
                                    }
                                }
                            }
                        }
                    }
                    STATUE = STATUS_STATIC;
                }
            } else {
                lastStaticStamp = stamp;
                STATUE = STATUS_STATIC;
            }
            mX = x;
            mY = y;
            mZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void saveImage(Context context, Bitmap bmp) {
        // 首先保存图片
        // File appDir = new File(Environment.getExternalStorageDirectory(), "boss66Im");
//        File appDir = new File(context.getFilesDir().getPath(), "boss66Im");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
        writeToSDFile();
        String fileName = System.currentTimeMillis() + ".jpg";
        imgFile = new File(savePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(imgFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    private void createFileWithByte(byte[] bytes) {
        writeToSDFile();
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        String imageName = System.currentTimeMillis() + ".jpg";
        imgFile = new File(savePath,
                imageName);
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
//            if (!imgFile.exists()) {
//                imgFile.mkdir();//如果路径不存在就先创建路径
//            }
            // 如果文件存在则删除
//            if (imgFile.exists()) {
//                imgFile.delete();
//            }
            // 在文件系统中根据路径创建一个新的空文件
            //imgFile.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(imgFile);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void makeRootDir(String filePath) {
        File file = null;
        String newPath = null;
        String[] path = filePath.split("/");
        for (int i = 0; i < path.length; i++) {
            if (newPath == null) {
                newPath = path[i];
            } else {
                newPath = newPath + "/" + path[i];
            }
            file = new File(newPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    //sdcard的写操作
    public void writeToSDFile() {
        try {
            makeRootDir(savePath);//先创建文件夹
        } catch (Exception e) {
// TODO: handle exception
        }
    }

    private Bitmap byteToBitmap(BitmapFactory.Options options, byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private PicDataDialog dataDialog = null;

    private void showPicDialog() {
        dataDialog = new PicDataDialog(this, picArray, type);
        dataDialog.setOnSubmitClick(this);
        dataDialog.setOnPictureAction(new PicDataDialog.OnPictureAction() {
            @Override
            public void onRequestAddNewPicture() {
                if (ContextCompat.checkSelfPermission(CameraHideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CameraHideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // 提示用户如果想要正常使用，要手动去设置中授权。
                        ToastUtils.showLong("请在 设置-应用管理 中开启此应用的储存授权。");
                    } else {
                        ActivityCompat.requestPermissions(CameraHideActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    Timber.i("不需要授权 ");
                    // 进行正常操作
//                    dataDialog.dismiss();
                    if (TextUtils.equals(type, TYPE_PIC)) {
                        initImagePicker();
                    } else {
//                        IntentUtil.startActivityForResult(CameraHideActivity.this, VideoRecordActivity.class, ACTIVITY_REQUEST_VIDEO_PATH, false);
                        Intent intent = new Intent(context, ImageGridActivity.class);
                        startActivityForResult(intent, 102);
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

    private void initImagePicker() {
        compressPicSize = 0;
        picArray.remove(0);
        Intent intent = new Intent(context, ImageSelectActivity.class);
        if (picArray != null && picArray.size() != 0) {
            intent.putExtra("pices", (ArrayList<String>) picArray);
        }
        startActivityForResult(intent, 103);
    }

    int compressPicSize = 0;

//    private void initImagePicker() {
//        compressPicSize = 0;
//        picArray.remove(0);
//        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
//            @Override
//            public void onStart() {
//                Timber.i("onStart: 开启");
//            }
//
//            @Override
//            public void onSuccess(List<String> photoList) {
//                Timber.i("onSuccess: 返回数据");
//                // 直接赋值给传入的list
//                picArray.clear();
//                picArray.add("head");
//                picArray.addAll(photoList);
//                Timber.d("返回图片集合： %s", picArray);
//                loadIngShow();
//                for (int i = 0; i < photoList.size(); i++) {
//                    final String originFilePath = photoList.get(i);
//                    me.shaohui.advancedluban.Luban.compress(CameraHideActivity.this, new File(originFilePath))
//                            .putGear(Luban.THIRD_GEAR)
//                            .launch(new CompressImage(originFilePath, photoList.size()));
//                }
//            }
//
//            @Override
//            public void onCancel() {
//                Timber.i("onCancel: 取消");
//            }
//
//            @Override
//            public void onFinish() {
//                Timber.i("onFinish: 结束");
//            }
//
//            @Override
//            public void onError() {
//                Timber.e("onError: 出错");
//            }
//        };
//        GalleryConfig galleryConfig = new GalleryConfig.Builder().imageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
//                Glide.with(activity).load(path).into(galleryImageView);
//            }
//
//            @Override
//            public void clearMemoryCache() {
//
//            }
//        })    // ImageLoader 加载框架（必填）
//                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
//                .pathList(picArray)                         // 记录已选的图片
//                // 是否多选   默认：false
//                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
//                .provider("com.guapi.fileprovider")   // provider(必填)
//                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
//                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
//                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
//                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
//                .filePath(getCacheDir().getAbsolutePath() + "/pictures")          // 图片存放路径
//                .build();
//        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
//    }

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
//                showPicDialog();
                if (dataDialog != null && dataDialog.isShowing()) {
                    dataDialog.refreshDailog(picArray);
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            loadIngDismiss();
            ToastUtils.showLong("图片处理出错");
            finish();
        }
    }

    private void uploadPicture(Dialog dialog, String message) {
//        AMapLocation location = BaseApp.getInstance().getUserLocation();
//        if (location == null) {
//            ToastUtils.showLong("无法定位，请退出重试");
//            return;
//        }
        if (TextUtils.isEmpty(address)) {
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
        Log.i("info", "=================file:" + file.length());
        Timber.d("需要上传的图片路径:%s", Arrays.asList(files));

        loadIngShow();
        Http.createPic(CameraHideActivity.this, TYPE_PIC, message,
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
                }, file, files);
    }

    private String img_path;

    public void getBitmapsFromVideo() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoKey);
// 取得视频的长度(单位为毫秒)
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
// 取得视频的长度(单位为秒)
        int seconds = Integer.valueOf(time) / 1000;
        String str = FileUtils.getFileNameFromPath(videoKey);
        String fileName = str.substring(0, str.indexOf("."));
// 得到每一秒时刻的bitmap比如第一秒,第二秒
        Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        String path = Constants.CACHE_IMG_DIR + fileName + ".jpeg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            img_path = path;
        }
    }

    private void uploadVideo(Dialog dialog, String message) {
//        AMapLocation location = BaseApp.getInstance().getUserLocation();
//        if (location == null) {
//            ToastUtils.showLong("无法定位，请退出重试");
//            return;
//        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showLong("无法定位，请退出重试");
            return;
        }
        dialog.dismiss();
        File pointFile = new File(pointKey);
        File videoFile = new File(videoKey);
        File videoPicFile = new File(img_path);
        loadIngShow();
        Http.createVideo(CameraHideActivity.this, TYPE_VIDEO, message,
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
                }, videoFile, pointFile, videoPicFile);
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
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapImg != null) {
            bitmapImg = null;
        }
        tryRecycleAnimationDrawable(animationDrawable);
        animationDrawable = null;
//        ivFocus.setBackground(null);
        System.gc();
        releaseCamera();
//        if (videoFile != null) {
//            videoFile = null;
//        }
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//        if (dialogNum != null) {
//            dialogNum.dismiss();
//        }
        imgFile = null;
    }

    private void tryRecycleAnimationDrawable(AnimationDrawable animationDrawables) {
        if (animationDrawables != null) {
            animationDrawables.stop();
            for (int i = 0; i < animationDrawables.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawables.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawables.setCallback(null);

        }
    }
}
