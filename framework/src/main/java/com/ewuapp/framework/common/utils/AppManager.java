package com.ewuapp.framework.common.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Activity管理类
 * @author Jewel
 * @version 1.0
 * @since 2016/9/27 0027
 */

public final class AppManager {

	private static final String TAG = AppManager.class.getName();
	private static volatile AppManager mAppManager;

	private AppManager() {

	}

	public static AppManager getInstance() {
		if(mAppManager == null) {
			synchronized(AppManager.class) {
				if(mAppManager == null) {
					mAppManager = new AppManager();
				}
			}
		}
		return mAppManager;
	}


	/**
	 * 维护存活Activity的list
	 */
	private List<Activity> mSurvivalActivityList = Collections.synchronizedList(new LinkedList<Activity>());

	/**
	 * 是否有上级页面
	 * @return Boolean
	 */
	public boolean hadPreActivity() {
		if(mSurvivalActivityList.isEmpty() || mSurvivalActivityList.size() <= 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 添加Activity到管理列表
	 *
	 * @param activity 新的Activity
	 */
	public void push(Activity activity) {
		if(mSurvivalActivityList == null) {
			mSurvivalActivityList = Collections.synchronizedList(new LinkedList<Activity>());
		}

		mSurvivalActivityList.add(activity);
		Log.d(TAG, "add activity to manager :" + activity.getComponentName());
	}

	/**
	 * 从管理列表移除Activity
	 *
	 * @param activity 旧的Activity
	 */
	public void pop(Activity activity) {
		if(mSurvivalActivityList == null || mSurvivalActivityList.isEmpty()) {
			return;
		}

		if(activity != null && mSurvivalActivityList.contains(activity)) {
			mSurvivalActivityList.remove(activity);
			Log.d(TAG, "remove activity to manager :" + activity.getComponentName());
		}
	}

	/**
	 * @return 获取当前Activity
	 */
	public Activity current() {
		if(mSurvivalActivityList == null || mSurvivalActivityList.isEmpty()) {
			return null;
		}
		return mSurvivalActivityList.get(mSurvivalActivityList.size() - 1);
	}

	/**
	 * @return 获取当前最顶部activity的实例
	 */
	public Activity getTop() {
		Activity mBaseActivity;
		synchronized(mSurvivalActivityList) {
			final int size = mSurvivalActivityList.size() - 1;
			if(size < 0) {
				return null;
			}
			mBaseActivity = mSurvivalActivityList.get(size);
		}
		return mBaseActivity;
	}

	/**
	 * 按照指定类名找到activity
	 */
	public Activity find(Class<?> cls) {
		Activity targetActivity = null;
		if(mSurvivalActivityList != null) {
			for(Activity activity : mSurvivalActivityList) {
				if(activity.getClass().equals(cls)) {
					targetActivity = activity;
					break;
				}
			}
		}
		return targetActivity;
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finish(Class<?> cls) {
		if(mSurvivalActivityList == null || mSurvivalActivityList.isEmpty()) {
			return;
		}
		for(Activity activity : mSurvivalActivityList) {
			if(activity.getClass().equals(cls)) {
				finish(activity);
			}
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public void finish(Activity activity) {
		if(mSurvivalActivityList == null || mSurvivalActivityList.isEmpty()) {
			return;
		}
		if(activity != null) {
			mSurvivalActivityList.remove(activity);
			activity.finish();
			Log.d(TAG, "finish activity from manager :" + activity.getComponentName());
			activity = null;
		}
	}


	/**
	 * 结束所有Activity
	 */
	public void finishAll() {
		if(mSurvivalActivityList == null) {
			return;
		}
		for(Activity activity : mSurvivalActivityList) {
			activity.finish();
			Log.d(TAG, "finish all activity from manager :" + activity.getComponentName());
		}
		mSurvivalActivityList.clear();
	}

}
