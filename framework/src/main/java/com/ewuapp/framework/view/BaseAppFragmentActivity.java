package com.ewuapp.framework.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ewuapp.framework.common.http.RetrofitManager;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;

import io.reactivex.disposables.Disposable;

/**
 * FragmentActivity基类.
 *
 * @author jewelbao
 * @version 1.0
 * @since 2016/8/29
 */

public abstract class BaseAppFragmentActivity<P extends BasePresenterImpl, V extends BaseViewPresenterImpl> extends AppCompatActivity {

    protected P presenter;
    protected V vPresenter;

    protected Handler mainHandler = new Handler();

    protected Runnable lazyLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            delayLoadData();
        }
    };

    public Handler getMainHandler() {
        return mainHandler;
    }

    /**
     * @return 逻辑代理对象
     */
    @NonNull
    protected abstract P getPresent();


    /**
     * @return 视图代理对象
     */
    @NonNull
    protected abstract V getViewPresent();

    /**
     * 如果需要使用Activity+Fragment的方式,则不要重载该方法;<p>
     * 如果只是单纯使用Activity的方式,则可以重载该方法,重载该方法后可以无需重载{@link AppCompatActivity#onCreate(Bundle)}
     *
     * @return 布局文件ID
     */
    protected abstract int getContentViewID();

    /**
     * 如果使用Activity+Fragment的方式并且重载了{@link BaseAppFragmentActivity#getContentViewID()},那么此方法必须重载,否则有可能报空指针<p>
     * 如果只是单纯使用Activity的方式,则该方法无效。
     *
     * @return {@link BaseAppFragmentActivity#getContentViewID()}返回的布局中Fragment的ID
     */
    protected abstract int getFragmentContentID();

    /**
     * 延迟初始化，耗时操作请在此方法中执行
     */
    protected void delayLoadData() {
    }

    protected void reload() {
    }


    /**
     * 普通初始化,区别{@link BaseAppFragmentActivity#delayLoadData()}
     */
    protected void initView(Bundle savedInstanceState) {
    }


    /**
     * 处理其他组件传递过来的Intent数据
     *
     * @param intent 其他组件传递过来的Intent数据
     */
    protected void handleIntent(Intent intent) {
    }

    /**
     * 更新标题栏
     */
    protected void setUpToolbar() {
    }

    /**
     * 添加Fragment
     *
     * @param fragment BaseFragment
     */
    public void addFragment(Fragment fragment) {
        addFragment(getFragmentContentID(), fragment);
    }

    /**
     * 添加Fragment
     *
     * @param fragment BaseFragment
     */
    public void addFragment(@android.support.annotation.IdRes int containerViewId, Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(containerViewId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 移除BaseFragment
     */
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            removeFragment();
        } else {
            IntentUtil.finishActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String key = this.getPackageName() + "." + this.getClass().getSimpleName();
        RetrofitManager.remove(key);
    }

    protected void addDisposable(Disposable disposable) {
        String key = this.getPackageName() + "." + this.getClass().getSimpleName();
        RetrofitManager.add(key, disposable);
    }

}
