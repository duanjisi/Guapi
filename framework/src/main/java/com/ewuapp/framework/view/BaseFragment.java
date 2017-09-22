package com.ewuapp.framework.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Fragment基类
 *
 * @author jewelbao
 * @version 2.0
 * @since 2016/8/29
 */
@SuppressWarnings("unused")
public abstract class BaseFragment<P extends BasePresenterImpl, V extends BaseViewPresenterImpl> extends Fragment {

    protected Handler mHandler = new Handler();
    public static final String KEY_STATE = "saveState";
    /**
     * 宿主Activity
     */
    protected Activity mActivity;

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * 保存的Bundle
     */
    protected Bundle mSavedState;
    /**
     * 刷新状态
     */
    private boolean refreshData = true;


    private P presenter;
    private V vPresenter;

    /**
     * 获取逻辑代理对象
     */
    public P getPresent(){
        return presenter;
    }

    /**
     * 获取视图代理对象,此方法在{@link BaseFragment#getPresent()}之前执行
     */
    public V getViewPresent(){
        return vPresenter;
    }

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public void setViewPresenter(V presenter) {
        this.vPresenter = presenter;
    }


    /**
     * 初始化所有视图,此操作发生在View创建完之后
     *
     * @param view              {@link BaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}返回的View.
     * @param saveInstanceState 如果不为空，应该使用saveInstanceState来获取回收之前保存的状态.
     */
    protected void initView(View view, Bundle saveInstanceState) {
    }

    /**
     * 延迟加载数据，只会执行一次，且在viewpager中装载了多个fragment生效，在fragment切换时执行，该方法在{@link Fragment#setUserVisibleHint(boolean)}中执行；
     * 若想要每次在用户可视界面时执行，可在此方法执行前调用{@link BaseFragment#setRefreshData(boolean)}
     */
    protected void delayLoadData(){}

    /**
     * 延迟加载数据，在Activity切换时执行，该方法在{@link Fragment#onResume()}中执行
     */
    protected void reloadDate(){}

    /**
     * 获取布局文件ID
     *
     * @return 布局文件ID
     */
    protected abstract int getLayoutID();

    /**
     * 获取宿主Activity
     *
     * @return 宿主Activity
     */
    protected Activity getHoldingActivity() {
        return mActivity;
    }

    /**
     * 获取当前根视图
     * @return View
     */
    protected View getRootView() {
        return mRootView;
    }

    /**
     * 处理其他组件传递过来的Bundle,已做了非空验证，如果没执行该方法，说明Bundle为空
     *
     * @param bundle  其他组件传递过来的Bundle
     */
    protected void handleArguments(Bundle bundle) {
    }

    /**
     * 若要实现状态保存，请重写此方法,与{@link BaseFragment#restoreState(Bundle)}绑定使用
     * @return Bundle
     */
    protected Bundle saveState() {
        return null;
    }

    /**
     * 若要实现状态恢复，请重写此方法,与{@link BaseFragment#saveState()}绑定使用
     * @param  savedState 已保存的Bundle
     */
    protected void restoreState(Bundle savedState) {
    }

    /**
     * {@link Fragment#setUserVisibleHint(boolean)}衍生方法
     * @param isVisibleToUser 是否用户可见
     */
    protected void onUserVisible(boolean isVisibleToUser) {}

    /**
     * 保存状态
     */
    private void saveStateToArguments() {
        if(getView() != null) {
            mSavedState = saveState();
        }
        if(mSavedState != null) {
            Bundle bundle = getArguments();
            bundle.putBundle(KEY_STATE, mSavedState);
        }
    }

    /**
     * 恢复保存的状态
     * @return 是否有保存的状态
     */
    private boolean restoreStateFromArguments() {
        Bundle bundle = getArguments();
        if(bundle == null) {
            return false;
        }
        mSavedState = bundle.getBundle(KEY_STATE);
        if(mSavedState != null) {
            restoreState(mSavedState);
            return true;
        }
        return false;
    }


    /**
     * @param refresh 设置是否刷新数据，只在{@link Fragment#setUserVisibleHint(boolean)}生效
     */
    public void  setRefreshData(boolean refresh) {
        refreshData = refresh;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(BaseFragment.class.getSimpleName(), "onAttach");

        if(getArguments() != null) {
            handleArguments(getArguments());
        }

        if(vPresenter == null) {
            vPresenter = getViewPresent();
        }

        if(presenter == null) {
            presenter = getPresent();
        }
        if(context != null) {
            presenter.setContext(context);
        }
        this.mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(BaseFragment.class.getSimpleName(), "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if(parent != null) {
            parent.removeView(mRootView);
        }

        ButterKnife.bind(this, mRootView);
        Log.d(BaseFragment.class.getSimpleName(), "onCreateView");
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(BaseFragment.class.getSimpleName(), "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(BaseFragment.class.getSimpleName(), "onActivityCreated");

        if(vPresenter == null) {
            vPresenter = getViewPresent();
        }

        if(presenter == null) {
            presenter = getPresent();
        }
        if(getActivity() != null) {
            presenter.setContext(getActivity());
        }
        if(getArguments() != null) {
            handleArguments(getArguments());
        }
        if(!restoreStateFromArguments()) { // 默认第一次进来是没有任何保存状态的
            initView(mRootView, savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(BaseFragment.class.getSimpleName(), "onResume");
        if(getActivity() == null) {
            return;
        }
        ((BaseAppFragmentActivity)getActivity()).getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadDate();
            }
        }, 500);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(BaseFragment.class.getSimpleName(), "onSaveInstanceState");
        saveStateToArguments();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(BaseFragment.class.getSimpleName(), "onViewStateRestored");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(BaseFragment.class.getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(BaseFragment.class.getSimpleName(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(BaseFragment.class.getSimpleName(), "onDestroyView");
        refreshData = true;
        saveStateToArguments();
        ButterKnife.unbind(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {  // 该方法在单个Activity中会优先于生命周期执行
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(BaseFragment.class.getSimpleName(), "setUserVisibleHint");

        if(getArguments() != null) {
            handleArguments(getArguments());
        }

        if(vPresenter == null) {
            vPresenter = getViewPresent();
        }

        if(presenter == null) {
            presenter = getPresent();
        }
        if(getActivity() != null) {
            presenter.setContext(getActivity());
        }

        if(isVisibleToUser && refreshData) {
            refreshData = false;
            delayLoadData();
        }

        onUserVisible(isVisibleToUser);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(BaseFragment.class.getSimpleName(), "onDetach");
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch(NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(BaseFragment.class.getSimpleName(), "onDestroy");
    }
}
