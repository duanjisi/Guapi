package com.ewuapp.framework.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewuapp.framework.R;
import com.ewuapp.framework.common.http.RetrofitManager;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public abstract class BasicFragment extends Fragment {

    protected BaseActivity context= (BaseActivity) getActivity();
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = setContentView(inflater, getViewId());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 调用该办法可避免重复加载UI
     */
    public View setContentView(LayoutInflater inflater, int resId) {
        if (rootView == null) {
            rootView = inflater.inflate(resId, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    protected boolean onBackKeyPressed() {
        return false;
    }

    protected void finish() {
        context.finish();
    }

    protected void finishSimple() {
        context.finish();
    }


    public void startActivity(Bundle bundle, Class<?> target) {
        context.startActivity(bundle, target);
    }

    public void startForResult(Bundle bundle, int requestCode, Class<?> target) {
        Intent intent = new Intent(context, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
       context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    protected abstract int getViewId();

    protected abstract void init();

    protected void addDisposable(Disposable disposable) {
        String key = getActivity().getPackageName() + "." + this.getClass().getSimpleName();
        RetrofitManager.add(key, disposable);
    }
}
