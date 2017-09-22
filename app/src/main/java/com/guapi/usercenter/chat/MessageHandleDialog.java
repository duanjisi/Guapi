package com.guapi.usercenter.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.guapi.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author : zhouyx
 * Date   : 2016/9/29
 * 消息框长按弹窗
 */
public class MessageHandleDialog {

    private int dialogWidth, dialogHeight, paddingTop, maxHeight;
    private PopupWindow popupWindow;
    private Callback callback;

    public MessageHandleDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_message_handle, null);
        ButterKnife.bind(this, view);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        PaintDrawable drawable = new PaintDrawable(context.getResources().getColor(R.color.transparent));
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        dialogWidth = 56;
        dialogHeight = 40;
        paddingTop = 10;

        Activity activity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        maxHeight = screenHeight - 65 - dialogHeight;
    }

    public void show(View parent, int x, int y) {
        int showLocationX = x - dialogWidth;
        int showLocationY = y + paddingTop;
        if (showLocationY > maxHeight) {
            showLocationY = maxHeight;
        }
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, showLocationX, showLocationY);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    @OnClick({R.id.delete, R.id.copy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                if (callback != null) {
                    callback.delete();
                }
                break;
            case R.id.copy:
                if (callback != null) {
                    callback.copy();
                }
                break;
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void delete();

        void copy();
    }

}
