package com.guapi.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.BaseApp;
import com.guapi.R;
import com.guapi.model.response.GPResponse;
import com.guapi.tool.Global;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.util.PermissonUtil.PermissionUtil;
import com.guapi.widget.scan.CameraManager;
import com.guapi.widget.scan.CameraPreview;
import com.guapi.widget.scan.CircleImageView;
import com.guapi.widget.scan.RoundImageView;
import com.listener.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

import static com.guapi.tool.Global.TYPE_HB;

/**
 * Created by Johnny on 2017-09-09.
 */
public class CatchActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> implements SensorEventListener {
    @Bind(R.id.rl_preciew)
    RelativeLayout rl_preciew;
    //    @Bind(R.id.ll_thread)
//    LinearLayout ll_thread;
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.iv_focus)
    ImageView ivFocus;
    //    @Bind(R.id.iv_thread)
//    ImageView iv_thread;
//    @Bind(R.id.iv_thread_bg)
//    ImageView iv_thread_bg;
    @Bind(R.id.riv_bg)
    RoundImageView roundImageView;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.iv_point)
    ImageView ivPoint;
    @Bind(R.id.iv_success)
    ImageView iv_success;
    @Bind(R.id.iv_user)
    CircleImageView ivUser;

    private ImageLoader imageLoader;
    private PermissionListener permissionListener;
    private GPResponse.GpListBean bean;
    private Calendar mCalendar;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_STATIC = 1;
    public static final int STATUS_MOVE = 2;
    private int STATUE = STATUS_NONE;
    private int mX, mY, mZ;
    public static final int DELEY_DURATION = 3500;
    private int sceenH;
    private String type = TYPE_HB;
    boolean canFocus = false;
    boolean canFocusIn = false;  //内部是否能够对焦控制机制
    boolean isFocusing = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private CameraPreview mPreview;
    private boolean isLighOn = false;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager mCameraManager;
    private Handler autoFocusHandler;
    private boolean previewing = true, isTakePic = false, isFirstImgBlack;
    private long lastStaticStamp = 0;
    private Bitmap localBitmap, newBitmap;
    private Mat oneMat, twoMat;
    private int cameraNum = 0;
    private double comPH;
    private AnimationDrawable animationDrawable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    getGuapiDetails();
                    break;
                case 0x01:
                    if (localBitmap != null) {
                        localBitmap = ThumbnailUtils.extractThumbnail(localBitmap, 450, 450);
                        isFirstImgBlack = getBitmapColor(localBitmap);
                        oneMat = new Mat();
                        Utils.bitmapToMat(localBitmap, oneMat);
                        oneMat = getMat(oneMat);
                    }
                    break;
                case 0x02:
                    lastStaticStamp = System.currentTimeMillis();
                    if (newBitmap != null) {
                        Bitmap bitmap = ThumbnailUtils.extractThumbnail(newBitmap, 450, 450);
                        if (isFirstImgBlack) {
                            if (getBitmapColor(bitmap)) {
//                                showToast("匹配成功", true);
                                playSucessGif();
                            } else {
                                noMatchDo();
                            }
                        } else {
                            if (getBitmapColor(bitmap)) {
                                noMatchDo();
                                return;
                            }
                            newBitmap = null;
                            twoMat = new Mat();
                            Utils.bitmapToMat(bitmap, twoMat);
                            twoMat = getMat(twoMat);
                            if (oneMat != null && twoMat != null)
                                comPH = comPareHist(oneMat, twoMat);
                            if (comPH >= 0.3 || (cameraNum >= 4 && comPH > (0.3 - 0.05 * cameraNum))) {
                                cameraNum = 0;
//                                getServerData();
//                                showToast("匹配成功", true);
                                playSucessGif();
                            } else {
                                cameraNum++;
                                noMatchDo();
                            }
                        }
                    } else {
                        noMatchDo();
                    }
                    break;
            }
        }
    };

    private void getGuapiDetails() {
        if (bean != null) {
            Bundle bundle = new Bundle();
//            bundle.putSerializable(Global.KEY_OBJ, bean);
            bundle.putString("gpId", bean.getGpId());
            bundle.putString("from","CatchActivity");
            IntentUtil.startActivityWithBundle(this, GPCommentActivity.class, bundle, false);
            finish();
        }
    }

    private void noMatchDo() {
//        pb_load.setVisibility(View.GONE);
//        showToast("图片匹配失败TAT，再试下吧", false);
        previewing = true;
        if (mCamera != null) {
            mCamera.startPreview();
        } else {
            initViewParams();
        }
        autoFocusHandler.postDelayed(doAutoFocus, 1000);
//        YoYo.with(Techniques.RotateOut).duration(1000).repeat(10).playOn(ivFocus);
    }

    /**
     * 比较来个矩阵的相似度
     *
     * @param mat1
     * @param mat2
     * @return
     */
    public static double comPareHist(Mat mat1, Mat mat2) {
        double target = Imgproc.compareHist(mat1, mat2,
                Imgproc.CV_COMP_CORREL);
        return target;
    }

    private Mat getMat(Mat mat1) {
        Mat srcMat = new Mat();
        Imgproc.cvtColor(mat1, srcMat, Imgproc.COLOR_BGR2GRAY);
        srcMat.convertTo(srcMat, CvType.CV_32F);
        return srcMat;
    }

    private boolean getBitmapColor(Bitmap bitmap) {
        boolean isBlack = false;
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
        float mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        float count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                int color = bitmap.getPixel(j, i);
                if (color == 0xFF000000) {
                    count++;
                }
            }
        }
        if (count == 0) {
            isBlack = false;
        } else {
            float aa = mArrayColorLengh / count;
            if (aa > 1.5) {
                isBlack = false;
            } else {
                float percent = count / mArrayColorLengh;
                if (percent > 0.9) {
                    isBlack = true;
                }
            }
        }
        return isBlack;
    }

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
        return R.layout.activity_catch_camera;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        sceenH = com.guapi.tool.Utils.getScreenHeight(this);
        ivFocus.setImageResource(R.drawable.center_point_bg);
        animationDrawable = (AnimationDrawable) ivFocus.getDrawable();

//        RelativeLayout.LayoutParams threadParam = (RelativeLayout.LayoutParams) iv_thread_bg.getLayoutParams();
//        threadParam.height = sceenH / 4;
//        threadParam.width = sceenH / 4;
//        iv_thread_bg.setLayoutParams(threadParam);
//        RelativeLayout.LayoutParams threadbgParam = (RelativeLayout.LayoutParams) iv_thread.getLayoutParams();
//        threadbgParam.height = sceenH / 4;
//        threadbgParam.width = sceenH / 4;
//        iv_thread.setLayoutParams(threadbgParam);

        autoFocusHandler = new Handler();
        mSensorManager = (SensorManager) BaseApp.getInstance().getSystemService(Activity.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        tvTip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按住
                        roundImageView.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动
                        break;
                    case MotionEvent.ACTION_UP:
                        //松开
                        roundImageView.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            bean = (GPResponse.GpListBean) intent.getSerializableExtra(Global.KEY_OBJ);
            imageLoader.displayImage(bean.getUserImagUrl(), ivUser, ImageLoaderUtils.getDisplayImageOptions());
            String imgUrl = bean.getPointPicture();
            if (!TextUtils.isEmpty(imgUrl)) {
//                Glide.with(context).load(imgUrl).error(R.drawable.zf_default_message_image).into(iv_thread);
                imageLoader.displayImage(imgUrl, roundImageView, ImageLoaderUtils.getDisplayImageOptions());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            localBitmap = Glide.with(context)
                                    .load(imgUrl)
                                    .asBitmap() //必须
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0x01);
                    }
                }).start();
            }
        }
        getPermission();
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("找瓜皮");
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
            }
        });
    }

    private void getPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
            }

            @Override
            public void onRequestPermissionSuccess() {
                initViewParams();
            }

            @Override
            public void onRequestPermissionError() {
                showToast(getString(R.string.giving_camera2_permissions), true);
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_CHAT_CAMERA //相机权限
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        type = getIntent().getStringExtra(Global.KEY_STR);
    }

    @Override
    protected void onStart() {
        super.onStart();
        canFocus = true;
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("info", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("info", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        animationDrawable.start();
//        getMainHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                YoYo.with(Techniques.RotateOut).duration(1000).repeat(10).playOn(ivFocus);
//            }
//        }, 2000);
    }

    //openCV4Android 需要加载用到
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("info", "OpenCV loaded successfully");
//                    mOpenCvCameraView.enableView();
//                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

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
        mCamera.stopPreview();
        isLighOn = false;
    }

    private void initViewParams() {
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
        if (mCamera != null) {
            parameters = mCamera.getParameters();
            // 设置预览照片的大小
            List<Camera.Size> supportedPictureSizes =
                    parameters.getSupportedPictureSizes();// 获取支持保存图片的尺寸
            if (supportedPictureSizes != null && supportedPictureSizes.size() > 0) {
                Camera.Size pictureSize = com.guapi.tool.Utils.getPictureSize(this, supportedPictureSizes);
//                parameters.setRotation(90);
                if (this.getResources().getConfiguration().orientation !=
                        Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    parameters.setRotation(90);
                } else {
                    parameters.set("orientation", "landscape");
                }

                parameters.setPictureSize(pictureSize.width, pictureSize.height);
                Log.i("descriptorMatcher", "pictureSize.width:" + pictureSize.width + "  pictureSize.height:" + pictureSize.height);
                mCamera.setParameters(parameters);
            }
            if (mPreview == null) {
                mPreview = new CameraPreview(this, mCamera, mPreviewCallback, autoFocusCB);
            }
            if (rl_preciew.getChildCount() > 0) {
                rl_preciew.removeAllViews();
            }
            rl_preciew.addView(mPreview);
        }
    }

    /**
     * 获取相机内容回调方法
     */
    Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.i("mPreviewCallback", "jinlai");
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] bytes, Camera camera) {
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    mCamera.stopPreview();
                }
//                pb_load.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            newBitmap = Glide.with(context)
                                    .load(bytes)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (OutOfMemoryError error) {
                            newBitmap = null;
                            System.gc();
                        }
                        handler.sendEmptyMessage(0x02);
                    }
                }).start();
            } catch (Exception e) {
                Log.i("descriptorMatcher:", "error:" + e.getMessage());
                e.printStackTrace();
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
                if (mCamera != null) {
                    try {
                        mCamera.autoFocus(autoFocusCB);
                    } catch (Exception e) {
                        isTakePic = true;
                        previewing = false;
                    }
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

//            int second = mCalendar.get(Calendar.SECOND);// 53

            if (STATUE != STATUS_NONE) {
                int px = Math.abs(mX - x);
                int py = Math.abs(mY - y);
                int pz = Math.abs(mZ - z);
                double value = Math.sqrt(px * px + py * py + pz * pz);
                if (value > 1.4) {
                    //Log.i("onSensorChanged", "检测手机在移动");
                    STATUE = STATUS_MOVE;
                    lastStaticStamp = stamp;
                } else {
                    //Log.i("onSensorChanged", "检测手机静止");
                    //上一次状态是move，记录静态时间点
                    canFocusIn = true;
                    if (canFocusIn) {
                        if (stamp - lastStaticStamp > DELEY_DURATION) {
                            //移动后静止一段时间，可以发生对焦行为
                            if (!isFocusing) {
                                canFocusIn = false;
                                if (!previewing && isTakePic && mCamera != null) {
                                    Log.i("onSensorChanged", "takePicture");
                                    mCamera.takePicture(null, null, mPictureCallback);
                                    isTakePic = false;
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

    private void playSucessGif() {
        try {
            if (iv_success != null) {
                iv_success.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(R.drawable.catch_guapi_succ0)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Integer, GlideDrawable>() {

                            @Override
                            public boolean onException(Exception arg0, Integer arg1,
                                                       Target<GlideDrawable> arg2, boolean arg3) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource,
                                                           Integer model, Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                // 计算动画时长
                                GifDrawable drawable = (GifDrawable) resource;
                                GifDecoder decoder = drawable.getDecoder();
                                int duration = 0;
                                for (int i = 0; i < drawable.getFrameCount(); i++) {
                                    duration += decoder.getDelay(i);
                                }
                                if (duration > 2000) {
                                    duration = 2000;
                                }
                                //发送延时消息，通知动画结束
                                handler.sendEmptyMessageDelayed(111,
                                        duration);
                                return false;
                            }
                        }) //仅仅加载一次gif动画
                        .into(new GlideDrawableImageViewTarget(iv_success, 1));
            }
        } catch (Exception e) {
            handler.sendEmptyMessageDelayed(111,
                    1000);
        } catch (OutOfMemoryError error) {
            System.gc();
            handler.sendEmptyMessageDelayed(111,
                    1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        tryRecycleAnimationDrawable(animationDrawable);
        animationDrawable = null;
//        ivFocus.setBackground(null);
        System.gc();
        if (newBitmap != null) {
            newBitmap = null;
        }
        if (localBitmap != null) {
            localBitmap = null;
        }
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
