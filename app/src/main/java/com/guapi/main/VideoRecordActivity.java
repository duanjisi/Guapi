package com.guapi.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.guapi.R;
import com.guapi.tool.Global;
import com.sh.shvideolibrary.compression.CompressListener;
import com.sh.shvideolibrary.compression.CompressorUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

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
 * @since 2017/7/16 0016
 */

public class VideoRecordActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {


    @Bind(R.id.iv_record)
    ImageView ivRecord;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.camera)
    CameraView camera;
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;

    private String videoPath;
    private static boolean isRecording = false;
    private CameraRunnable cameraRunnable;
    private static long startRecordingTime = 0;

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
        return R.layout.activity_video_record;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ivRecord.setVisibility(View.GONE);
        cameraRunnable = new CameraRunnable(this, camera, tvTip);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                camera.start();
                camera.setJpegQuality(20);
                camera.setCropOutput(true);
                camera.setFlash(CameraKit.Constants.FLASH_AUTO);
                camera.setVideoQuality(CameraKit.Constants.VIDEO_QUALITY_LOWEST);
                camera.setCameraListener(new CameraListener() {

                    @Override
                    public void onVideoTaken(File video) {
                        super.onVideoTaken(video);
                        String videoOutputPath = getCacheDir().getAbsolutePath() + "/video" + File.separator + System.currentTimeMillis() + ".mp4";
                        if (FileUtils.createOrExistsFile(videoOutputPath)) {
                            loadIngShow();
                            Timber.d("视频路径:%s", videoOutputPath);
                            CompressorUtils compressorUtils = new CompressorUtils(video.getAbsolutePath(), videoOutputPath, VideoRecordActivity.this);
                            compressorUtils.execCommand(new CompressListener() {
                                @Override
                                public void onExecSuccess(String message) {
                                    File videoFile = new File(videoOutputPath);
                                    float length = videoFile.length() / 1024f;
                                    Timber.d("视频压缩成功：%s", message);
                                    Timber.d("视频大小：%s", length + "k");
                                    loadIngDismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(Global.KEY_VIDEO_PATH, videoOutputPath);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void onExecFail(String reason) {
                                    Timber.d("视频压缩失败：%s", reason);
                                    loadIngDismiss();
                                    finish();
                                }

                                @Override
                                public void onExecProgress(String message) {

                                }
                            });
                        }
                    }
                });
                ivRecord.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setBackPressed(this);
        toolBarView.setDrawable(ToolBarView.TEXT_RIGHT, R.mipmap.ic_flash);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.ic_cancel);
        toolBarView.setOnRightClickListener(new ToolBarView.OnBarRightClickListener() {
            @Override
            public void onRightClick(View v) {
                camera.toggleFlash();
            }
        });
    }

    @OnClick(R.id.iv_record)
    public void onViewClicked() {
        if (isRecording) {
            camera.stopRecordingVideo();
            isRecording = false;
            startRecordingTime = 0;
            tvTip.setVisibility(View.GONE);
            getMainHandler().removeCallbacks(cameraRunnable);
        } else {
            startRecordingTime = System.currentTimeMillis();
            camera.startRecordingVideo();
            isRecording = true;
            getMainHandler().postDelayed(cameraRunnable, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
        getMainHandler().removeCallbacks(cameraRunnable);
    }

    private static class CameraRunnable implements Runnable {
        private WeakReference<VideoRecordActivity> activityWeakReference;
        private WeakReference<CameraView> cameraViewWeakReference;
        private WeakReference<TextView> textViewWeakReference;

        CameraRunnable(VideoRecordActivity activity, CameraView view, TextView tip) {
            activityWeakReference = new WeakReference<VideoRecordActivity>(activity);
            cameraViewWeakReference = new WeakReference<CameraView>(view);
            textViewWeakReference = new WeakReference<TextView>(tip);
        }

        @Override
        public void run() {
            if (activityWeakReference.get() == null || cameraViewWeakReference.get() == null) {
                return;
            }
            long time = (System.currentTimeMillis() - startRecordingTime) / 1000L;
            if (time >= 10 && isRecording) {
                cameraViewWeakReference.get().stopRecordingVideo();
                startRecordingTime = 0;
                isRecording = false;
                textViewWeakReference.get().setVisibility(View.GONE);
                activityWeakReference.get().getMainHandler().removeCallbacks(this);
            } else {
                activityWeakReference.get().getMainHandler().postDelayed(this, 1000);
                textViewWeakReference.get().setVisibility(View.VISIBLE);
                textViewWeakReference.get().setText("正在录制视频" + time + "秒");
            }
        }
    }
}
