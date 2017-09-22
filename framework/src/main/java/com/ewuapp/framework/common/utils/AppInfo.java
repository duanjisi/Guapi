package com.ewuapp.framework.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @author Jewel
 * @version 1.0
 * @since 2016/9/16 0016
 */

public class AppInfo {
    private static final String TAG = "AppInfo";

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return verName;
    }

//	/**
//	 * 得到软件显示版本号
//	 *
//	 * @param context 上下文
//	 * @return 当前版本信息
//	 */
//	public static int getVerCode(Context context) {
//		try {
//			String packageName = context.getPackageName();
//			return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
//		} catch(PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

    /**
     * @param versionName 三段式数字取名
     * @return 三段式对应的版本号
     */
    public static long getVerCodeByVerName(String versionName) {
        String[] versionNames = versionName.split("\\.");
        Integer[] versions = new Integer[versionNames.length];

        if (versionNames.length != 3) { // 在此app中，规定了版本名为三段式取名规则。
            throw new RuntimeException("versionName is illegal. Note that in this app, the versionName contained in the column must be well formed! Such as '1.10.1'");
        }

        for (int i = 0; i < versionNames.length; i++) {
            if (!TextUtils.isDigitsOnly(versionNames[i])) { // 在此app中，规定了版本名的三段式取名只能是数字。
                throw new RuntimeException("versionName is illegal. Note that in this app, the versionName contained in the column must be well formed! Such as '1.10.1'");
            }
            versions[i] = Integer.parseInt(versionNames[i]);
        }

        return (long) (versions[0] * 1000000 + versions[1] * 1000 + versions[2]);
    }

    /**
     * 程序是否在前台运行
     *
     * @return boolean
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


}
