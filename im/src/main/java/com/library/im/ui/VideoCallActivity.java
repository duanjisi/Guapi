///**
// * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.library.im.ui;
//
//import java.util.UUID;
//
//import com.hyphenate.chat.EMCallManager.EMCameraDataProcessor;
//import com.hyphenate.chat.EMCallManager.EMVideoCallHelper;
//import com.hyphenate.chat.EMCallStateChangeListener;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.media.EMLocalSurfaceView;
//import com.hyphenate.media.EMOppositeSurfaceView;
//import com.hyphenate.util.PathUtil;
//import com.library.im.R;
//import com.library.im.controller.HxHelper;
//
//import android.hardware.Camera;
//import android.media.AudioManager;
//import android.media.RingtoneManager;
//import android.media.SoundPool;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class VideoCallActivity extends CallActivity implements OnClickListener {
//
//    private boolean isMuteState;
//    private boolean isHandsfreeState;
//    private boolean isAnswered;
//    private boolean endCallTriggerByMe = false;
//    private boolean monitor = true;
//
//    private TextView callStateTextView;
//
//    private LinearLayout comingBtnContainer;
//    private Button refuseBtn;
//    private Button answerBtn;
//    private Button hangupBtn;
//    private ImageView muteImage;
//    private ImageView handsFreeImage;
//    private TextView nickTextView;
//    private Chronometer chronometer;
//    private LinearLayout voiceContronlLayout;
//    private RelativeLayout rootContainer;
//    private RelativeLayout btnsContainer;
//    private LinearLayout topContainer;
//    private LinearLayout bottomContainer;
//    private TextView monitorTextView;
//    private TextView netwrokStatusVeiw;
//
//    private Handler uiHandler;
//
//    /**
//     * 正在通话中
//     */
//    private boolean isInCalling;
//    boolean isRecording = false;
//    private Button recordBtn;
//    private Button switchCameraBtn;
//    private SeekBar YDeltaSeekBar;
//    private EMVideoCallHelper callHelper;
//    private Button toggleVideoBtn;
//
//    private BrightnessDataProcess dataProcessor = new BrightnessDataProcess();
//
//    // dynamic adjust brightness
//    class BrightnessDataProcess implements EMCameraDataProcessor {
//        byte yDelta = 0;
//        synchronized void setYDelta(byte yDelta) {
//            Log.d("VideoCallActivity", "brigntness uDelta:" + yDelta);
//            this.yDelta = yDelta;
//        }
//
//        // data size is width*height*2
//        // the first width*height is Y, second part is UV
//        // the storage layout detailed please refer 2.x demo CameraHelper.onPreviewFrame
//        @Override
//        public synchronized void onProcessData(byte[] data, Camera camera, int width, int height) {
//            int wh = width * height;
//            for (int i = 0; i < wh; i++) {
//                int d = (data[i] & 0xFF) + yDelta;
//                d = d < 16 ? 16 : d;
//                d = d > 235 ? 235 : d;
//                data[i] = (byte)d;
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null){
//        	finish();
//        	return;
//        }
//        setContentView(R.layout.em_activity_video_call);
//
//        HxHelper.getInstance().isVideoCalling = true;
//        callType = 1;
//
//        getWindow().addFlags(
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        uiHandler = new Handler();
//
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
//        rootContainer = (RelativeLayout) findViewById(R.id.root_layout);
//        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
//        answerBtn = (Button) findViewById(R.id.btn_answer_call);
//        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
//        muteImage = (ImageView) findViewById(R.id.iv_mute);
//        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        nickTextView = (TextView) findViewById(R.id.tv_nick);
//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
//        btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);
//        topContainer = (LinearLayout) findViewById(R.id.ll_top_container);
//        bottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);
//        monitorTextView = (TextView) findViewById(R.id.tv_call_monitor);
//        netwrokStatusVeiw = (TextView) findViewById(R.id.tv_network_status);
//        recordBtn = (Button) findViewById(R.id.btn_record_video);
//        switchCameraBtn = (Button) findViewById(R.id.btn_switch_camera);
//        YDeltaSeekBar = (SeekBar) findViewById(R.id.seekbar_y_detal);
////        toggleVideoBtn = (Button) findViewById(R.id.btn_toggle_video_stream);
//
//
//        refuseBtn.setOnClickListener(this);
//        answerBtn.setOnClickListener(this);
//        hangupBtn.setOnClickListener(this);
//        muteImage.setOnClickListener(this);
//        handsFreeImage.setOnClickListener(this);
//        rootContainer.setOnClickListener(this);
//        recordBtn.setOnClickListener(this);
//        switchCameraBtn.setOnClickListener(this);
////        toggleVideoBtn.setOnClickListener(this);
//        YDeltaSeekBar.setOnSeekBarChangeListener(new YDeltaSeekBarListener());
//
//        msgid = UUID.randomUUID().toString();
//        // 获取通话是否为接收方向的
//        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
//        username = getIntent().getStringExtra("username");
//
//        // 设置通话人
//        nickTextView.setText(username);
//
//        // 显示本地图像的surfaceview
//        localSurface = (EMLocalSurfaceView) findViewById(R.id.local_surface);
//        localSurface.setZOrderMediaOverlay(true);
//        localSurface.setZOrderOnTop(true);
//
//        // 显示对方图像的surfaceview
//        oppositeSurface = (EMOppositeSurfaceView) findViewById(R.id.opposite_surface);
//
//        // 设置通话监听
//        addCallStateListener();
//        if (!isInComingCall) {// 拨打电话
//            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
//            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);
//
//            comingBtnContainer.setVisibility(View.INVISIBLE);
//            hangupBtn.setVisibility(View.VISIBLE);
//            String st = getResources().getString(R.string.Are_connected_to_each_other);
//            callStateTextView.setText(st);
//            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
//            handler.sendEmptyMessage(MSG_CALL_MAKE_VIDEO);
//        } else { // 有电话进来
//            voiceContronlLayout.setVisibility(View.INVISIBLE);
//            localSurface.setVisibility(View.INVISIBLE);
//            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            audioManager.setSpeakerphoneOn(true);
//            ringtone = RingtoneManager.getRingtone(this, ringUri);
//            ringtone.play();
//            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
//        }
//        //获取callhelper对象，需要写在callManager().setSurfaceView方法后面
//        callHelper = EMClient.getInstance().callManager().getVideoCallHelper();
//
//        EMClient.getInstance().callManager().setCameraDataProcessor(dataProcessor);
//    }
//
//    class YDeltaSeekBarListener implements SeekBar.OnSeekBarChangeListener {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            dataProcessor.setYDelta((byte)(20.0f * (progress - 50) / 50.0f));
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//        }
//
//    }
//
//    /**
//     * 设置通话状态监听
//     */
//    void addCallStateListener() {
//        callStateListener = new EMCallStateChangeListener() {
//
//            @Override
//            public void onCallStateChanged(CallState callState, final CallError error) {
//                // Message msg = handler.obtainMessage();
//                switch (callState) {
//
//                case CONNECTING: // 正在连接对方
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            callStateTextView.setText(R.string.Are_connected_to_each_other);
//                        }
//
//                    });
//                    break;
//                case CONNECTED: // 双方已经建立连接
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            callStateTextView.setText(R.string.have_connected_with);
//                        }
//
//                    });
//                    break;
//
//                case ACCEPTED: // 电话接通成功
//                    handler.removeCallbacks(timeoutHangup);
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                if (soundPool != null)
//                                    soundPool.stop(streamID);
//                            } catch (Exception e) {
//                            }
//                            openSpeakerOn();
//                            ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
//                                    ? R.string.direct_call : R.string.relay_call);
//                            handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//                            isHandsfreeState = true;
//                            isInCalling = true;
//                            chronometer.setVisibility(View.VISIBLE);
//                            chronometer.setBase(SystemClock.elapsedRealtime());
//                            // 开始记时
//                            chronometer.start();
//                            nickTextView.setVisibility(View.INVISIBLE);
//                            callStateTextView.setText(R.string.In_the_call);
//                            recordBtn.setVisibility(View.VISIBLE);
//                            callingState = CallingState.NORMAL;
//                            startMonitor();
//                        }
//
//                    });
//                    break;
//                case NETWORK_UNSTABLE:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            netwrokStatusVeiw.setVisibility(View.VISIBLE);
//                            if(error == CallError.ERROR_NO_DATA){
//                                netwrokStatusVeiw.setText(R.string.no_call_data);
//                            }else{
//                                netwrokStatusVeiw.setText(R.string.network_unstable);
//                            }
//                        }
//                    });
//                    break;
//                case NETWORK_NORMAL:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            netwrokStatusVeiw.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                    break;
//                case VIDEO_PAUSE:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "VIDEO_PAUSE", 0).show();
//                        }
//                    });
//                    break;
//                case VIDEO_RESUME:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "VIDEO_RESUME", 0).show();
//                        }
//                    });
//                    break;
//                case VOICE_PAUSE:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "VOICE_PAUSE", 0).show();
//                        }
//                    });
//                    break;
//                case VOICE_RESUME:
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "VOICE_RESUME", 0).show();
//                        }
//                    });
//                    break;
//                case DISCONNNECTED: // 电话断了
//                    handler.removeCallbacks(timeoutHangup);
//                    final CallError fError = error;
//                    runOnUiThread(new Runnable() {
//                        private void postDelayedCloseMsg() {
//                            uiHandler.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    saveCallRecord();
//                                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
//                                    animation.setDuration(800);
//                                    rootContainer.startAnimation(animation);
//                                    finish();
//                                }
//
//                            }, 200);
//                        }
//
//                        @Override
//                        public void run() {
//                            chronometer.stop();
//                            callDruationText = chronometer.getText().toString();
//                            String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
//                            String s2 = getResources().getString(R.string.Connection_failure);
//                            String s3 = getResources().getString(R.string.The_other_party_is_not_online);
//                            String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
//                            String s5 = getResources().getString(R.string.The_other_party_did_not_answer);
//
//                            String s6 = getResources().getString(R.string.hang_up);
//                            String s7 = getResources().getString(R.string.The_other_is_hang_up);
//                            String s8 = getResources().getString(R.string.did_not_answer);
//                            String s9 = getResources().getString(R.string.Has_been_cancelled);
//
//                            if (fError == CallError.REJECTED) {
//                                callingState = CallingState.BEREFUESD;
//                                callStateTextView.setText(s1);
//                            } else if (fError == CallError.ERROR_TRANSPORT) {
//                                callStateTextView.setText(s2);
//                            } else if (fError == CallError.ERROR_INAVAILABLE) {
//                                callingState = CallingState.OFFLINE;
//                                callStateTextView.setText(s3);
//                            } else if (fError == CallError.ERROR_BUSY) {
//                                callingState = CallingState.BUSY;
//                                callStateTextView.setText(s4);
//                            } else if (fError == CallError.ERROR_NORESPONSE) {
//                                callingState = CallingState.NORESPONSE;
//                                callStateTextView.setText(s5);
//                            }else if (fError == CallError.ERROR_LOCAL_VERSION_SMALLER || fError == CallError.ERROR_PEER_VERSION_SMALLER){
//                                callingState = CallingState.VERSION_NOT_SAME;
//                                callStateTextView.setText(R.string.call_version_inconsistent);
//                            }  else {
//                                if (isAnswered) {
//                                    callingState = CallingState.NORMAL;
//                                    if (endCallTriggerByMe) {
////                                        callStateTextView.setText(s6);
//                                    } else {
//                                        callStateTextView.setText(s7);
//                                    }
//                                } else {
//                                    if (isInComingCall) {
//                                        callingState = CallingState.UNANSWERED;
//                                        callStateTextView.setText(s8);
//                                    } else {
//                                        if (callingState != CallingState.NORMAL) {
//                                            callingState = CallingState.CANCED;
//                                            callStateTextView.setText(s9);
//                                        } else {
//                                            callStateTextView.setText(s6);
//                                        }
//                                    }
//                                }
//                            }
//                            postDelayedCloseMsg();
//                        }
//
//                    });
//
//                    break;
//
//                default:
//                    break;
//                }
//
//            }
//        };
//        EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.btn_refuse_call) {
//            refuseBtn.setEnabled(false);
//            handler.sendEmptyMessage(MSG_CALL_REJECT);
//
//        } else if (i == R.id.btn_answer_call) {
//            answerBtn.setEnabled(false);
//            openSpeakerOn();
//            if (ringtone != null)
//                ringtone.stop();
//
//            callStateTextView.setText("正在接听...");
//            handler.sendEmptyMessage(MSG_CALL_ANSWER);
//            handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//            isAnswered = true;
//            isHandsfreeState = true;
//            comingBtnContainer.setVisibility(View.INVISIBLE);
//            hangupBtn.setVisibility(View.VISIBLE);
//            voiceContronlLayout.setVisibility(View.VISIBLE);
//            localSurface.setVisibility(View.VISIBLE);
//
//        } else if (i == R.id.btn_hangup_call) {
//            hangupBtn.setEnabled(false);
//            chronometer.stop();
//            endCallTriggerByMe = true;
//            callStateTextView.setText(getResources().getString(R.string.hanging_up));
//            if (isRecording) {
//                callHelper.stopVideoRecord();
//            }
//            handler.sendEmptyMessage(MSG_CALL_END);
//
//        } else if (i == R.id.iv_mute) {
//            if (isMuteState) {
//                // 关闭静音
//                muteImage.setImageResource(R.drawable.em_icon_mute_normal);
//                EMClient.getInstance().callManager().resumeVoiceTransfer();
//                isMuteState = false;
//            } else {
//                // 打开静音
//                muteImage.setImageResource(R.drawable.em_icon_mute_on);
//                EMClient.getInstance().callManager().pauseVoiceTransfer();
//                isMuteState = true;
//            }
//
//        } else if (i == R.id.iv_handsfree) {
//            if (isHandsfreeState) {
//                // 关闭免提
//                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
//                closeSpeakerOn();
//                isHandsfreeState = false;
//            } else {
//                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//                openSpeakerOn();
//                isHandsfreeState = true;
//            }
//
//        } else if (i == R.id.btn_record_video) {
//            if (!isRecording) {
//                callHelper.startVideoRecord(PathUtil.getInstance().getVideoPath().getAbsolutePath());
//                isRecording = true;
//                recordBtn.setText(R.string.stop_record);
//            } else {
//                String filepath = callHelper.stopVideoRecord();
//                isRecording = false;
//                recordBtn.setText(R.string.recording_video);
//                Toast.makeText(getApplicationContext(), String.format(getString(R.string.record_finish_toast), filepath), 1).show();
//            }
//
////        case R.id.btn_toggle_video_stream:
////            EMClient.getInstance().callManager().pauseVideoTransfer();
////            break;
//        } else if (i == R.id.root_layout) {
//            if (callingState == CallingState.NORMAL) {
//                if (bottomContainer.getVisibility() == View.VISIBLE) {
//                    bottomContainer.setVisibility(View.GONE);
//                    topContainer.setVisibility(View.GONE);
//
//                } else {
//                    bottomContainer.setVisibility(View.VISIBLE);
//                    topContainer.setVisibility(View.VISIBLE);
//
//                }
//            }
//
//        } else if (i == R.id.btn_switch_camera) {
//            handler.sendEmptyMessage(MSG_CALL_SWITCH_CAMERA);
//
//
//        } else {
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        HxHelper.getInstance().isVideoCalling = false;
//        stopMonitor();
//        if(isRecording){
//            callHelper.stopVideoRecord();
//            isRecording = false;
//        }
//        localSurface = null;
//		oppositeSurface = null;
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        callDruationText = chronometer.getText().toString();
//        super.onBackPressed();
//    }
//
//    /**
//     * 方便开发测试，实际app中去掉显示即可
//     */
//    void startMonitor(){
//        new Thread(new Runnable() {
//            public void run() {
//                while(monitor){
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            monitorTextView.setText("宽x高："+callHelper.getVideoWidth()+"x"+callHelper.getVideoHeight()
//                                    + "\n延迟：" + callHelper.getVideoTimedelay()
//                                    + "\n帧率：" + callHelper.getVideoFramerate()
//                                    + "\n丢包数：" + callHelper.getVideoLostcnt()
//                                    + "\n本地比特率：" + callHelper.getLocalBitrate()
//                                    + "\n对方比特率：" + callHelper.getRemoteBitrate());
//
//                        }
//                    });
//                    try {
//                        Thread.sleep(1500);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }).start();
//    }
//
//    void stopMonitor(){
//        monitor = false;
//    }
//
//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        if(isInCalling){
//            EMClient.getInstance().callManager().pauseVideoTransfer();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(isInCalling){
//            EMClient.getInstance().callManager().resumeVideoTransfer();
//        }
//    }
//
//}
