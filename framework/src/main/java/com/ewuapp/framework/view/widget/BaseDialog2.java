package com.ewuapp.framework.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.ewuapp.framework.R;


/**
 * 对话框基类
 *
 * @author Jewel
 * @version 1.2
 * @since 2016/9/5 0005
 */

public class BaseDialog2 extends Dialog {

    public static final int MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;
    public static final float DEFAULT_DIM = 0.4f;

    public BaseDialog2(Context context) {
        this(context, R.style.loading_dialog, false);
    }

    protected BaseDialog2(Context context, boolean cancelable) {
        this(context, R.style.loading_dialog, cancelable);
    }

    public BaseDialog2(Context context, int themeResId, boolean cancelable) {
        this(context, themeResId, cancelable, true);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(true);
    }

    public BaseDialog2(Context context, int themeResId, boolean cancelable, boolean canceledOnTouchOutside) {
        super(context, themeResId);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = getDimAmount();
        layoutParams.width = getWidth();
        layoutParams.height = getHeight();
        layoutParams.gravity = getGravity();

        window.setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
    }

    /**
     * 绑定Dialog的自定义layout资源
     *
     * @return layout资源ID
     */
    @LayoutRes
    protected int getContentView() {
        return -1;
    }

    /**
     * 初始化操作
     */
    protected void initView() {
    }

    /**
     * 背景暗淡系数，通过重载该返回设置该系数
     *
     * @return {@link  BaseDialog2#DEFAULT_DIM}
     */
    protected float getDimAmount() {
        return DEFAULT_DIM;
    }

    /**
     * 对话框宽度，通过重载该方法设置该系数，默认{@link BaseDialog2#MATCH_PARENT}
     *
     * @return {@link BaseDialog2#WRAP_CONTENT}, {@link BaseDialog2#MATCH_PARENT}, 其他自定义的宽度
     */
    protected int getWidth() {
        return MATCH_PARENT;
    }

    /**
     * 对话框高度，通过重载该方法设置该系数，默认{@link BaseDialog2#WRAP_CONTENT}
     *
     * @return {@link BaseDialog2#WRAP_CONTENT}, {@link BaseDialog2#MATCH_PARENT}, 其他自定义的高度
     */
    protected int getHeight() {
        return MATCH_PARENT;
    }

    /**
     * 对话框显示位置，通过重载该方法设置该系数，默认{@link Gravity#CENTER}
     *
     * @return 参考 {@link Gravity}
     */
    protected int getGravity() {
        return Gravity.CENTER;
    }
}
