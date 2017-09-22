package com.ewuapp.framework.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ewuapp.framework.R;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;

import java.util.List;

import butterknife.ButterKnife;

/**
 * FragmentActivity基类，对外开放.默认加载方式为Activity+Fragment.<p>
 * 如果不想使用Fragment,则重载{@link BaseFragmentActivity#getContentViewID()}即可,无需重载{@link BaseFragmentActivity#onCreate(Bundle)},或者继承{@link BaseActivity}
 *
 * @author jewelbao
 * @version 2.0
 * @since 2016/8/29
 */

public abstract class BaseFragmentActivity<P extends BasePresenterImpl, V extends BaseViewPresenterImpl> extends BaseAppFragmentActivity<P, V> {


    protected abstract BaseFragment getFirstFragment();

    protected void initDataBeforePresenter(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        ButterKnife.bind(this);

        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        initDataBeforePresenter();

        if (vPresenter == null) {
            vPresenter = getViewPresent();
        }

        if (presenter == null) {
            presenter = getPresent();
            presenter.setContext(this);
        }
        setUpToolbar();
        initView(savedInstanceState);

        // 避免重复添加fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (null == fragmentList || fragmentList.isEmpty()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

		getWindow().getDecorView().postDelayed(new Runnable() {
			@Override
			public void run() {
				mainHandler.post(lazyLoadingRunnable);
			}
		}, 200);
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_base;
	}

    @Override
    protected int getFragmentContentID() {
        return R.id.fragment_container;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
    }
}
