package com.ewuapp.framework.presenter.Impl;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.ewuapp.framework.presenter.IBasePresenter;

/**
 * @author jewelbao
 * @version 1.0
 * @since 2016/8/31
 */

public class BasePresenterImpl implements IBasePresenter {

	protected Context mContext;
	protected FragmentManager fragmentManager;

	public BasePresenterImpl(FragmentManager fm) {
		this.fragmentManager = fm;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

}
