package com.guapi.main;

import android.content.Context;
import android.widget.ImageView;

import com.ewuapp.framework.view.widget.BaseDialog;
import com.guapi.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/7/10 0010
 */

public class FindPicDialog extends BaseDialog {

    @Bind(R.id.iv_point)
    ImageView ivPoint;

    public FindPicDialog(Context context) {
        super(context, R.style.base_dialog, true);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_money_success;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
