package com.ewuapp.framework.common.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ewuapp.framework.common.utils.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
 * <p>
 * 对请求进行预处理和简化每次都要写的线程步骤
 */

public class RxSchedulers {
    /**
     * 基本请求
     */
    public static <T> FlowableTransformer<T, T> mainThread(final Context context) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull Subscription subscription) throws Exception {
                                // 如果无网络，则直接取消
                                if (!NetworkUtil.isConnected(context)) {
                                    cancel(subscription);
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 带进度条的请求
     */
    public static <T> FlowableTransformer<T, T> mainThread(final Context context, final ProgressDialog dialog) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull final Subscription subscription) throws Exception {
                                if (!NetworkUtil.isConnected(context)) {
                                    cancel(subscription);
                                } else {
                                    // 启动进度显示，取消进度时会取消请求
                                    if (dialog != null) {
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                subscription.cancel();
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static void cancel(@NonNull Subscription subscription) {
        subscription.cancel();
        MsgEvent msgEvent = new MsgEvent(Constants.NET_CODE_CONNECT, Constants.CONNECT_EXCEPTION);
        EventBus.getDefault().post(msgEvent);
    }
}
