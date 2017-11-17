package com.guapi.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.BaseApp;
import com.guapi.R;
import com.guapi.model.ImageTool;
import com.guapi.model.response.GPResponse;
import com.guapi.tool.Global;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.util.PermissonUtil.PermissionUtil;
import com.guapi.widget.scan.CameraManager;
import com.guapi.widget.scan.CameraPreview;
import com.guapi.widget.scan.CircleImageView;
import com.guapi.widget.scan.RoundImageView;
import com.listener.PermissionListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import okhttp3.Response;

import static com.guapi.tool.Global.TYPE_HB;

//import yxr.com.library.SimilarityHelper;

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
    CircleImageView ivPoint;
    @Bind(R.id.iv_success)
    ImageView iv_success;
    @Bind(R.id.iv_user)
    CircleImageView ivUser;


    private int[] resIds = {R.drawable.catch_guapi_succ0, R.drawable.catch_guapi_succ1};

    private int[] resId2s = {R.drawable.catch_guapi_succ2, R.drawable.catch_guapi_succ3};

    private int resDrawableId = 0;

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
    private int sceenH, sceenW;
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
    private int edgeLength = 0;
    private int orientations = 0;
    private OrientationEventListener mOrientationListener;
    private File file1, file2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0x01:
//                    if (localBitmap != null) {
//                        localBitmap = ThumbnailUtils.extractThumbnail(localBitmap, 450, 450);
//                        isFirstImgBlack = getBitmapColor(localBitmap);
//                        oneMat = new Mat();
//                        Utils.bitmapToMat(localBitmap, oneMat);
//                        oneMat = getMat(oneMat);
//                    }
//                    break;
                case 0x02:
                    matcherBitmap();
//                    lastStaticStamp = System.currentTimeMillis();
//                    if (newBitmap != null) {
//                        Bitmap bitmap = ThumbnailUtils.extractThumbnail(newBitmap, 450, 450);
//                        if (isFirstImgBlack) {
//                            if (getBitmapColor(bitmap)) {
////                                showToast("匹配成功", true);
//                                playSucessGif();
//                            } else {
//                                noMatchDo();
//                            }
//                        } else {
//                            if (getBitmapColor(bitmap)) {
//                                noMatchDo();
//                                return;
//                            }
////                            matchBitmap();
//                            twoMat = new Mat();
//                            Utils.bitmapToMat(bitmap, twoMat);
//                            twoMat = getMat(twoMat);
//                            if (oneMat != null && twoMat != null)
//                                comPH = comPareHist(oneMat, twoMat);
//                            if (comPH >= 0.3 || (cameraNum >= 4 && comPH > (0.3 - 0.05 * cameraNum))) {
//                                cameraNum = 0;
////                                showToast("匹配成功", true);
//                                noMatchDo();
//                                playSucessGif();
//                            } else {
//                                if (cameraNum >= 4) {
//                                    cameraNum = 0;
//                                }
//                                cameraNum++;
//                                noMatchDo();
//                            }
//                            Log.i("info", "==================comPH:" + comPH);
//                            showToast("==============相似度:" + comPH, true);
//
////                            || (cameraNum >= 3 && comPH > (0.35 - 0.05 * cameraNum))
////                            if (comPH >= 0.35 || (cameraNum >= 4 && comPH >= (0.35 - 0.06 * cameraNum))) {
////                                cameraNum = 0;
////                                noMatchDo();
////                                playSucessGif();
////                            } else {
////                                if (cameraNum >= 4) {
////                                    cameraNum = 0;
////                                }
////                                cameraNum++;
////                                noMatchDo();
////                            }
//                        }
//                    } else {
//                        noMatchDo();
//                    }
                    break;
            }
        }
    };

    private void matcherBitmap() {
        if (file1 != null && file2 != null) {
//            int retCode = new ImageMatcher().DoMatcher(com.guapi.tool.Utils.bitmapToBytes(localBitmap), com.guapi.tool.Utils.bitmapToBytes(newBitmap));
//            if (retCode == -1) {
//                showToast("Not Match", true);
//            } else if (retCode == 0) {
//                showToast("Match", true);
//            } else if (retCode == 1) {
//                showToast("Like Success", true);
//            } else if (retCode == 2) {
//                showToast("Maybe Like", true);
//            }

            OkGo.post("http://120.76.245.82/SimilarImageMatcher/preLoadPic.do")    //
                    .params("sourcePic", file1)      // 可以添加文件上传
                    .params("MatcherPic", file2)      // 可以添加文件上传
                    .execute(new StringCallback() {

                        @Override
                        public void onSuccess(String s, okhttp3.Call call, Response response) {
                            if (s.contains("-1")) {
                                showToast("匹配失败...", true);
                            } else if (s.contains("0") || s.contains("1")) {
                                showToast("匹配成功...", true);
                                playSucessGif();
                            } else {
                                showToast("匹配失败...", true);
                            }
                            newBitmap = null;
                            noMatchDo();
                        }

                        @Override
                        public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        }
                    });

//            Http.matcherPic(context, file1, file2, new CallBack<Result>() {
//                @Override
//                public void handlerSuccess(Result data) {
//                    newBitmap = null;
//                    noMatchDo();
//                    showToast(data.getRetVal(), true);
//                }
//
//                @Override
//                public void fail(int code, String message) {
//                    newBitmap = null;
//                    noMatchDo();
//                    showToast(message, true);
//                }
//            });
        }
    }

//    private SimilarityHelper similarityHelper;
//    private void matchBitmap() {
//        if (localBitmap != null) {
//            Bitmap bitmap = Utils.createBitmap(localBitmap);
//            if (localBitmap != null && newBitmap != null) {
//                String similar = BitmapCompare.similarity(localBitmap, newBitmap);
//                showToast("相似度：" + similar, true);
//                newBitmap = null;
//                noMatchDo();
////                similarityHelper.similarity(bitmap, newBitmap, new SimilarityCallBack() {
////                    @Override
////                    public void onSimilarityStart() {
////
////                    }
////
////                    @Override
////                    public void onSimilaritySuccess(int similarity, int different) {
////                        float similar = ((float) similarity / (similarity + different) * 100);
////                        showToast("相似度：" + similar + "%", true);
////                        newBitmap = null;
////                        noMatchDo();
////                        if (similar >= 60) {
////                            playSucessGif();
////                        }
////                    }
////
////                    @Override
////                    public void onSimilarityError(String reason) {
////                        showToast(reason, true);
////                        noMatchDo();
////                    }
////                });
//            }
//        }
//    }

    private void getGuapiDetails() {
        if (bean != null) {
            Bundle bundle = new Bundle();
//            bundle.putSerializable(Global.KEY_OBJ, bean);
            bundle.putString("gpId", bean.getGpId());
            bundle.putString("from", "CatchActivity");
            IntentUtil.startActivityWithBundle(this, GPCommentActivity.class, bundle, false);
            finish();
        }
    }


    public double converHist(Bitmap bitmap1, Bitmap bitmap2) {
        Mat OneMat = new Mat();
        Mat TowMat = new Mat();
        Utils.bitmapToMat(bitmap1, OneMat);
        Utils.bitmapToMat(bitmap2, TowMat);
        return compareImage(OneMat, TowMat);
    }

    private double compareImage(Mat natOne, Mat natTow) {
        Mat srcMat = new Mat();
        Mat desMat = new Mat();

        Imgproc.cvtColor(natOne, srcMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(natTow, desMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.equalizeHist(srcMat, desMat);

        srcMat.convertTo(srcMat, CvType.CV_32F);
        srcMat.convertTo(desMat, CvType.CV_32F);

        double target = Imgproc.compareHist(srcMat, desMat,
                Imgproc.CV_COMP_CORREL);
        return target;
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
        Log.i("info", "========================isBlack:" + isBlack);
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
//        sceenH = com.guapi.tool.Utils.getScreenHeight(this);
        ivFocus.setImageResource(R.drawable.center_point_bg);
        animationDrawable = (AnimationDrawable) ivFocus.getDrawable();
        sceenH = (int) (com.guapi.tool.Utils.getScreenHeight(context) * 0.8);
        sceenW = (int) (com.guapi.tool.Utils.getScreenWidth(context) * 0.8);
        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                orientations = orientation;
                Log.v("time", "现在是横屏" + orientation);
            }
        };
        edgeLength = com.guapi.tool.Utils.px2dip(context, ivPoint.getLayoutParams().width);
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
        iv_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuapiDetails();
            }
        });

        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            savePath = getFilesDir().getPath();
        } else {
            savePath = Environment.getExternalStorageDirectory() + "/guapi/";
        }

        file1 = new File(savePath, "source.jpg");
        file2 = new File(savePath, "matcher.jpg");

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
                            saveImage(context, localBitmap, file1);
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

    private String savePath;

    public void saveImage(Context context, Bitmap bmp, String fileName) {
        writeToSDFile();
//        String fileName = System.currentTimeMillis() + ".jpg";
        File imgFile = new File(savePath, fileName);
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

    public void saveImage(Context context, Bitmap bmp, File imgFile) {
        writeToSDFile();
        if (imgFile.exists()) {
            imgFile.delete();
        }
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

    //sdcard的写操作
    public void writeToSDFile() {
        try {
            makeRootDir(savePath);//先创建文件夹
        } catch (Exception e) {
// TODO: handle exception
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
                int roation = orientations;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                opts.inSampleSize = ImageTool.computeSampleSize(opts, -1, sceenW * sceenH);
                opts.inJustDecodeBounds = false;
                Bitmap bitmap = com.guapi.tool.Utils.ImageCrop(context, byteToBitmap(opts, bytes), true, edgeLength);
                newBitmap = com.guapi.tool.Utils.changeRoation(context, bitmap, roation, edgeLength);
                saveImage(context, newBitmap, file2);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            newBitmap = Glide.with(context)
//                                    .load(bytes)
//                                    .asBitmap()
//                                    .centerCrop()
//                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                                    .get();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (OutOfMemoryError error) {
//                            newBitmap = null;
//                            System.gc();
//                        }
//                        handler.sendEmptyMessage(0x02);
//                    }
//                }).start();
//                handler.sendEmptyMessage(0x02);
                handler.sendEmptyMessageDelayed(0x02, 500);
            } catch (Exception e) {
                Log.i("descriptorMatcher:", "error:" + e.getMessage());
                e.printStackTrace();
            }
        }
    };

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
                Random rnd = new Random();
                int rndint = rnd.nextInt(2);
                if (TextUtils.equals(bean.getType(), Global.TYPE_PIC)) {
                    resDrawableId = resIds[rndint];
                } else if (TextUtils.equals(bean.getType(), Global.TYPE_VIDEO)) {
                    resDrawableId = resId2s[rndint];
                } else if (TextUtils.equals(bean.getType(), Global.TYPE_HB)) {
                    resDrawableId = resId2s[0];
                }
                if (resDrawableId == 0) {
                    return;
                }
                iv_success.setVisibility(View.VISIBLE);
                Log.i("info", "=================playSucessGif()");
//                Glide.with(this).load(resDrawableId)
//                        .crossFade().into(iv_success);
                Glide.with(this)
                        .load(resDrawableId)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(iv_success);
            }
        } catch (Exception e) {
//            handler.sendEmptyMessageDelayed(111,
//                    1000);
        } catch (OutOfMemoryError error) {
            System.gc();
//            handler.sendEmptyMessageDelayed(111,
//                    1000);
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
