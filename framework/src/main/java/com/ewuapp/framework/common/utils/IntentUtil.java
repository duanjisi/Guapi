package com.ewuapp.framework.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 意图相关工具
 *
 * @author jewelbao
 * @version 1.0
 * @since 2016/9/1
 */

public class IntentUtil {

    /**
     * 界面跳转
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivity(Context currentActivity, Class<?> targetActivity, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


    /**
     * 界面跳转
     *
     * @param currentActivity 当前活动
     * @param action          目标活动
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivity(Context currentActivity, String action, boolean finish) {
        Intent intent = new Intent();
        intent.setAction(action);
        currentActivity.startActivity(intent);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    /**
     * 带Bundle数据传递的界面跳转
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param bundle          需要传递的数据
     * @param finish          是否结束当前活动
     */
    public static void startActivityWithBundle(Context currentActivity, Class<?> targetActivity, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


    /**
     * 带Bundle数据传递的界面跳转
     *
     * @param currentActivity 当前活动
     * @param action          目标活动
     * @param bundle          需要传递的数据
     * @param finish          是否结束当前活动
     */
    public static void startActivityWithBundle(Context currentActivity, String action, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    /**
     * 界面跳转并返回结果
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param RequestCode     请求码
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivityForResult(Context currentActivity, Class<?> targetActivity, int RequestCode, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        ((Activity) currentActivity).startActivityForResult(intent, RequestCode);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    /**
     * 带Bundle数据传递的界面跳转并返回结果
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param bundle          需要传递的数据
     * @param RequestCode     请求码
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivityWithBundleForResult(Context currentActivity, Class<?> targetActivity, Bundle bundle, int RequestCode, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        intent.putExtras(bundle);
        ((Activity) currentActivity).startActivityForResult(intent, RequestCode);
        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


    /**
     * 跳转页面--结束中间活动
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param refresh         是否刷新要跳转的界面
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivityWithBundleAndFinishOtherAll(Context currentActivity, String targetActivity, Bundle bundle, boolean refresh, boolean finish) {
        Intent intent = new Intent();
        intent.setAction(targetActivity);
        intent.putExtras(bundle);
        if (!refresh) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
        currentActivity.startActivity(intent);

        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


    /**
     * 跳转页面--结束中间活动
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param refresh         是否刷新要跳转的界面
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivityWithBundleAndFinishOtherAll(Context currentActivity, Class<?> targetActivity, Bundle bundle, boolean refresh, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        intent.putExtras(bundle);
        if (!refresh) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
        currentActivity.startActivity(intent);

        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    /**
     * 跳转页面--结束中间活动
     *
     * @param currentActivity 当前活动
     * @param targetActivity  目标活动
     * @param refresh         是否刷新要跳转的界面
     * @param finish          是否结束当前活动
     */
    @SuppressWarnings("unused")
    public static void startActivityAndFinishOtherAll(Context currentActivity, Class<?> targetActivity, boolean refresh, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, targetActivity);
        if (!refresh) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
        currentActivity.startActivity(intent);

        if (finish) {
            ((Activity) currentActivity).finish();
        }
//        ((Activity) currentActivity).overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    /**
     * 结束当前活动
     *
     * @param currentActivity activity
     */
    public static void finishActivity(Context currentActivity) {
        Activity parent = ((Activity) currentActivity).getParent();
        Activity topActivity = ((Activity) currentActivity);
        while (parent != null) {
            topActivity = parent;
            parent = parent.getParent();
        }
        topActivity.finish();
//        topActivity.overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);
    }


    /**
     * 延时结束activity  默认500ms
     *
     * @param currentActivity activity
     */
    public static void finishActivityDelay(final Context currentActivity) {
        finishActivityDelay(currentActivity, 500);
    }

    /**
     * 延时结束activity
     *
     * @param currentActivity activity
     * @param s               延迟结束时间
     */
    public static void finishActivityDelay(final Context currentActivity, long s) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finishActivity(currentActivity);
            }
        }, s);
    }
}
