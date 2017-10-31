package com.guapi;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
//import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.util.ArrayMap;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.Utils;
import com.ewuapp.framework.common.BaseApplication;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.RetrofitManager;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.common.utils.SdCardUtil;
import com.ewuapp.framework.common.utils.SharedPre;
import com.guapi.http.Http;
import com.guapi.model.response.QueryUserInfoByHxResponse;
import com.guapi.tool.JPushUtil;
import com.guapi.tool.PreferenceKey;
import com.guapi.usercenter.chat.ChatActivity;
import com.guapi.util.NotificationSoundUtil;
import com.library.im.cache.ContactsCacheUtils;
import com.library.im.cache.Process;
import com.library.im.controller.HxHelper;
import com.library.im.domain.EaseUser;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.Disposable;

//import android.support.annotation.RequiresApi;

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
 * @since 2017/6/15 0015
 */

public class BaseApp extends BaseApplication {

    private static BaseApp mApp;

    public static BaseApp getInstance() {
        if (mApp == null) {
            throw new RuntimeException("NullPointException caught at BaseApplication");
        }
        return mApp;
    }

    private AMapLocation userLocation;

    public AMapLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(AMapLocation userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //图片框架初始化
        GlideUtil.init(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        NotificationSoundUtil.init(this);
        initJPush();
        SharedPre.init(this, getPackageName());
        SdCardUtil.initFileDir(this);
        //初始化Hawk
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
        HxHelper.getInstance().init(this, ChatActivity.class, MainActivity.class, MainActivity.class);
        HxHelper.avatar = Hawk.get(PreferenceKey.AVATAR, "");
        Utils.init(this);
        ContactsCacheUtils.getInstance(this).setProcess(new Process() {
            @Override
            public EaseUser process(String key) {
                final EaseUser[] easeUser = new EaseUser[1];
                if (!CheckUtil.isNumeric(key)) {
                    try {
                        addDisposable(Http.queryUserInforByHx(getApplicationContext(), key, new CallBack<QueryUserInfoByHxResponse>() {
                            @Override
                            public void handlerSuccess(QueryUserInfoByHxResponse data) {
                                if (data != null) {
                                    easeUser[0] = new EaseUser(key);
                                    easeUser[0].setAvatar(data.getDataBean().getImageUrl());
                                    easeUser[0].setNick(data.getDataBean().getDestName());
                                }
                            }

                            @Override
                            public void fail(int code, String message) {
                            }
                        }));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                        return easeUser[0];
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return easeUser[0];
                } else {
                    return null;
                }
            }
        });
    }

    protected void addDisposable(Disposable disposable) {
        String key = this.getPackageName() + "." + this.getClass().getSimpleName();
        RetrofitManager.add(key, disposable);
    }

    @Override
    public String getBaseUrl() {
        return "http://120.26.94.214:8080/GP/";
    }

    //    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public ArrayMap<String, String> getRequestHeader() {
        String sessionId = Hawk.get(PreferenceKey.SESSIONID, "");
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("user_session", sessionId);
        return headers;
    }

    @Override
    public ArrayMap<String, String> getRequestParams() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(getApplicationContext());
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.gywm;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
//        builder.notificationDefaults = Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS; // 设置为铃声、震动、呼吸灯闪烁都要
//                | Notification.DEFAULT_SOUND;
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        JPushUtil.init(this);
    }

}
