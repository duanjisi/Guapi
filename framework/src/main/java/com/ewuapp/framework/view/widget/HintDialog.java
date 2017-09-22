package com.ewuapp.framework.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.R;
import com.ewuapp.framework.view.BaseActivity;
import butterknife.ButterKnife;


public class HintDialog extends Dialog {

    private Callback callback;
    private BaseActivity activity;
    private TextView textView;
    private TextView cancle;
    private TextView comfig;
    private TextView tishiTv;

    /**
     * @param context 上下文
     * @param content 提示的文字
     * @param btnText 按钮显示的文字，最多长度为两个
     */
    public HintDialog(Context context, String content, String[] btnText) {
        super(context, R.style.Dialog);
        activity = (BaseActivity) context;
        if(!activity.isFinishing()){
            this.setCanceledOnTouchOutside(true);
            setContentView(R.layout.dialog_outlogin);
            ButterKnife.bind(this);
            textView = (TextView) findViewById(R.id.textshow);
            cancle = (TextView) findViewById(R.id.cancel);
            comfig = (TextView) findViewById(R.id.commit);
            tishiTv = (TextView) findViewById(R.id.tv_tishi);
            textView.setText(content);
            if (btnText.length > 0) {
                if (btnText.length > 1) {
                    cancle.setText(btnText[0]);
                    comfig.setText(btnText[1]);
                } else {
                    cancle.setVisibility(View.GONE);
                    comfig.setText(btnText[0]);
                }
            } else {
                cancle.setVisibility(View.GONE);
                comfig.setVisibility(View.GONE);
            }
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    callback.cancle();
                }
            });
            comfig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    callback.callback();
                }
            });
        }
    }

    public void setTiShiText(String text) {
        tishiTv.setText(text);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void callback();

        void cancle();
    }
}
