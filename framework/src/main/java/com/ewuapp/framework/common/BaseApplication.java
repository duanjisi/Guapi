package com.ewuapp.framework.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.ArrayMap;
import com.ewuapp.framework.common.utils.AppManager;
import timber.log.Timber;

/**
 * @author Jewel
 * @version 1.0
 * @since 2016/9/6 0006
 */

public abstract class BaseApplication extends Application {
    private boolean isDebug = true;

    /**
     * 配置网络请求根路径
     */
    public abstract String getBaseUrl();

    /**
     * 配置网络请求头
     */
    public abstract ArrayMap<String, String> getRequestHeader();

    /**
     * 配置公共请求参数
     */
    public abstract ArrayMap<String, String> getRequestParams();

    private static BaseApplication mApp;

    public static BaseApplication getInstance() {
        if (mApp == null) {
            throw new RuntimeException("NullPointException caught at BaseApplication");
        }
        return mApp;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        initTimber();
        registerActivityManager();
    }

    /**
     * 初始化本地日志模式和内存分析插件
     */
    private void initTimber() {
        Timber.plant(new Timber.DebugTree());  // debug模式打印日志并使用本地内存泄漏跟踪
        //	LeakCanary.install(this);
    }

    /**
     * 生命周期管理
     */
    private void registerActivityManager() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getInstance().push(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
//                EventBus.getDefault().register(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                EventBus.getDefault().unregister(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getInstance().pop(activity);
            }
        });
    }
}
