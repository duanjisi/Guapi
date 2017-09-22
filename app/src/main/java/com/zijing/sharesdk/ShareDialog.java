package com.zijing.sharesdk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.guapi.R;

/**
 * 类描述:商品详情分享对话框
 * 作者:xues
 * 时间:2016年05月04日
 */
public class ShareDialog extends Dialog {
    LinearLayout llWechat, llWechatPyq;

    public enum ShareType {
        QQ, Wechat, Sina, WechatMoment;
    }

    private ShareCallBack callBack;

    public ShareDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_share);
        llWechat = (LinearLayout) findViewById(R.id.ll_wechat);
        llWechatPyq = (LinearLayout) findViewById(R.id.ll_wechat_pyq);
        setCanceledOnTouchOutside(true);
        llWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.callBack(ShareType.Wechat);
                    shareWechat();
                    dismiss();
                }
            }
        });
        llWechatPyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.callBack(ShareType.WechatMoment);
                    shareWechatMoment();
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //横向铺满手机屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.bottom_int_out_dialog_style); // 添加动画
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public void setShareCall(ShareCallBack call) {
        this.callBack = call;
    }

    /**
     * 网页分享到微信WeChat
     */
    public void shareWechat() {
    }

    /**
     * 网页分享到微信朋友圈WeChat Moment
     */
    public void shareWechatMoment() {
    }
}
