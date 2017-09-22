package com.guapi;

import android.os.Build;
import android.os.Environment;

/**
 * Created by Johnny on 2017-09-12.
 */
public class Constants {
    public static String VIDEO_CACHE_PATH;
    public static final String CACHE_IMG_DIR = Environment.getExternalStorageDirectory() + "/guapi/";

    static {
        VIDEO_CACHE_PATH = getBaseDir() + "video/";
    }

    private static String getBaseDir() {
        String baseDir = "";
        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            baseDir = BaseApp.getInstance().getFilesDir().getPath() + "/guapi/";
        } else {
            baseDir = Environment.getExternalStorageDirectory() + "/guapi/";
        }
        return baseDir;
    }

    public static final class Action {
        public static final String VIDEO_CACHE_SUCCESSED = "im.boss66.com.video.cache.successed";
        public static final String VIDEO_CACHE_FAILE = "im.boss66.com.video.cache.fail";
        public static final String CLOSE_HIDE_ACTIVITY = "im.boss66.com.close.hide.activity";
    }
}
