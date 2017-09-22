package com.ewuapp.framework.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.R;

public class DialogLoading extends Dialog {

    private TextView loadingLabel;

    public DialogLoading(Context context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.dialog_loading_layout);
        setCancelable(true);//不能通过点击返回键，消失
        setCanceledOnTouchOutside(false);
        loadingLabel = (TextView) findViewById(R.id.loading_text);
    }

    public void setDialogLabel(String label) {
        this.show();
        loadingLabel.setVisibility(View.VISIBLE);
        loadingLabel.setText(label);
    }
}
