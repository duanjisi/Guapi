package com.ewuapp.framework.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ewuapp.framework.R;


/**
 * Summary ：加载网络图片使用的工具类，基于Glide
 * Created by zhangdm on 2016/2/20.
 */
public class GlideUtil {
    public static final String TAG = "GlideUtil";
    /**
     * Glide的请求管理器类
     */
    private static RequestManager mRequestManager;
    private static Context mContext;

    /**
     * 初始化Glide工具
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        mRequestManager = Glide.with(context);
    }

    /**
     * Glide工具类是否已经初始化
     *
     * @return 已初始化则返回true
     */
    public static boolean isInit() {
        if (mContext == null || mRequestManager == null) {
            return false;
        }
        return true;
    }

    /**
     * 加载正方形的网络图片
     *
     * @param url       网络地址
     * @param imageView 目标控件
     */
    public static void loadPicture(String url, ImageView imageView) {
        loadPicture(url, imageView, R.drawable.default_image);
    }

    /**
     * 加载正方形的网络图片
     *
     * @param url        网络地址
     * @param imageView  目标控件
     * @param defaultImg 默认的图片 若不需要则输入-1
     */
    public static void loadPicture(String url, ImageView imageView, int defaultImg) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        DrawableRequestBuilder builder;
        if (CheckUtil.isNull(url)) {
            builder = mRequestManager
                    .load("")
                    .dontAnimate();
        } else {
            builder = mRequestManager
                    .load(url)
                    .dontAnimate();
        }

        if (defaultImg != -1) {
            builder = builder.placeholder(defaultImg);
        }
        builder.into(imageView);
    }

    public static void loadPicture(String url, GlideDrawableImageViewTarget listener, int defaultImg) {
        if (!isInit()) {
            return;
        }
        DrawableRequestBuilder builder = mRequestManager
                .load(url)
                .dontAnimate();
        if (defaultImg != -1) {
            builder = builder.placeholder(defaultImg);
        }
        builder.into(listener);
    }


    public static void clear() {
        Glide.get(mContext).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(mContext).clearDiskCache();
            }
        }).start();

    }

}
