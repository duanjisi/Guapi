package com.ewuapp.framework.common.http;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.DisposableSubscriber;

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

public class BaseSubscriber<T extends Result> extends DisposableSubscriber<T> {

    private CallBack<T> callBack;

    public BaseSubscriber(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onNext(T result) {
        //业务代码为成功则将具体的数据返回，否则利用EventBus将错误发出去
        if (result.getCode() == Constants.NET_CODE_SUCCESS) {
            if(callBack != null) callBack.handlerSuccess(result);
        } else {
            if(callBack!=null){
                callBack.fail(result.getCode(),result.getMsg());
            }
            EventBus.getDefault().post(new MsgEvent(result.getCode(), result.getMsg()));
        }
    }

    @Override
    public void onError(Throwable t) {
        MsgEvent msgEvent;
        // 处理常见的连接错误
        if (t instanceof SocketTimeoutException) {
            msgEvent = new MsgEvent(Constants.NET_CODE_SOCKET_TIMEOUT, Constants.SOCKET_TIMEOUT_EXCEPTION);
        } else if (t instanceof ConnectException) {
            msgEvent = new MsgEvent(Constants.NET_CODE_CONNECT, Constants.CONNECT_EXCEPTION);
        } else if (t instanceof UnknownHostException) {
            msgEvent = new MsgEvent(Constants.NET_CODE_UNKNOWN_HOST, Constants.UNKNOWN_HOST_EXCEPTION);
        } else {
            msgEvent = new MsgEvent(Constants.NET_CODE_ERROR, t.getMessage());
        }
        EventBus.getDefault().post(msgEvent);
    }

    @Override
    public void onComplete() {

    }
}
