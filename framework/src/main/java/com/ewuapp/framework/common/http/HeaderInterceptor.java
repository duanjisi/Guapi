package com.ewuapp.framework.common.http;

import android.util.ArrayMap;

import com.ewuapp.framework.common.BaseApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
 *
 * 定义请求头拦截器
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        ArrayMap<String, String> headers = BaseApplication.getInstance().getRequestHeader();
        //如果公共请求头不为空,则构建新的请求
        if (headers != null) {
            Request.Builder requestBuilder = originalRequest.newBuilder();

            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
            requestBuilder.method(originalRequest.method(), originalRequest.body());
            return chain.proceed(requestBuilder.build());
        }
        return chain.proceed(originalRequest);
    }
}
