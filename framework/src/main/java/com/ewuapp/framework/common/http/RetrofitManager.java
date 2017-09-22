package com.ewuapp.framework.common.http;

import android.util.ArrayMap;

import com.ewuapp.framework.common.BaseApplication;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

public class RetrofitManager {
    private static ArrayMap<String, CompositeDisposable> netManager = new ArrayMap<>();

    public static Retrofit getInstance() {
        return Instance.retrofit;
    }

    private static class Instance {
        private static String baseUrl = BaseApplication.getInstance().getBaseUrl();
        private static Retrofit retrofit = getRetrofit();

        private static Retrofit getRetrofit() {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            // debug模式添加log信息拦截
            if(BaseApplication.getInstance().isDebug()) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpBuilder.addInterceptor(interceptor);
            }
            httpBuilder.addInterceptor(new HeaderInterceptor());
            httpBuilder.addInterceptor(new ParamsInterceptor());
            // 设置重试
            httpBuilder.retryOnConnectionFailure(true);
            // 设置连接超时
            httpBuilder.connectTimeout(30, TimeUnit.SECONDS);
            //设置写超时
            httpBuilder.writeTimeout(60, TimeUnit.SECONDS);
            //设置读超时
            httpBuilder.readTimeout(60, TimeUnit.SECONDS);

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(baseUrl);
            retrofitBuilder.client(httpBuilder.build());
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            return retrofitBuilder.build();
        }
    }

    //为了避免错误的取消了，key建议使用packageName + className
    public static void add(String key, Disposable disposable) {
        if(netManager.containsKey(key)) {
            netManager.get(key).add(disposable);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            netManager.put(key, compositeDisposable);
        }
    }

    public static void remove(String key) {
        if(netManager.containsKey(key)) {
            CompositeDisposable compositeDisposable = netManager.get(key);
            compositeDisposable.clear();
        }
    }
}
