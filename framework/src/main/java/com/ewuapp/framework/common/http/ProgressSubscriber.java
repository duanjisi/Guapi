package com.ewuapp.framework.common.http;

import android.app.ProgressDialog;

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
 * @since 2017/6/14 0014
 */

public class ProgressSubscriber<T extends Result> extends BaseSubscriber<T> {

    private ProgressDialog dialog;

    public ProgressSubscriber(ProgressDialog dialog, CallBack<T> callBack) {
        super(callBack);
        this.dialog = dialog;
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        if(dialog != null) dialog.dismiss();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if(dialog != null) dialog.dismiss();
    }
}
