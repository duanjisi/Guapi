package com.ewuapp.framework.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.ewuapp.framework.R;
import com.ewuapp.framework.common.http.MsgEvent;
import com.ewuapp.framework.common.utils.AppManager;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.widget.DialogLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * 纯Activity基类，使用于不含Fragment的情形。
 *
 * @author jewelbao
 * @version 1.0
 * @since 2016/8/31
 */

public abstract class BaseActivity<P extends BasePresenterImpl, V extends BaseViewPresenterImpl> extends BaseAppFragmentActivity<P, V> {
    private Toast mToast;
    protected BaseActivity context;
    public DialogLoading loading;
    protected static final int FINSH_ACTIVITY = 1000;

    protected void beforeSetContentView() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeSetContentView();
        super.onCreate(savedInstanceState);
        context = this;
        AppManager.getInstance().push(this);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        loading = new DialogLoading(context);
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        presenter = getPresent();
        presenter.setContext(this);

        setUpToolbar();

        initView(savedInstanceState);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHandler.post(lazyLoadingRunnable);
            }
        }, 200);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void error(MsgEvent message) {
//        Toast.makeText(this, message.getMsg(), Toast.LENGTH_LONG).show();
    }

    public void loadIngShow() {
        if (loading != null) {
            loading.show();
        }
    }

    public void loadIngDismiss() {
        if (loading != null) {
            loading.dismiss();
            loading.cancel();
        }
    }

    @Override
    protected int getFragmentContentID() {
        return -1;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reload();
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        mainHandler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param resId  资源ID
     * @param length true为长时间，false为短时间
     * @return: void
     */
    protected void showToast(int resId, boolean length) {
        Toast.makeText(this, resId, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg    内容
     * @param length true为长时间，false为短时间
     * @return: void
     */
    protected void showToast(String msg, boolean length) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    protected String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 带回调的跳转
     *
     * @param bundle
     * @param requestCode
     * @param target
     */
    public void startForResult(Bundle bundle, int requestCode, Class<?> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    /**
     * 覆写startactivity方法，加入切换动画
     */
    public void startActivity(Bundle bundle, Class<?> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    /**
     * 重载
     */
    public void startActivity(Class<?> target) {
        startActivity(null, target);
    }

    public void showMessage(Object message) {
        showToast(message);
    }

    private void showToast(final Object message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("".equals(message) || message == null)
                    return;
                if (mToast == null) {
                    mToast = Toast.makeText(context, message + "", Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(message + "");
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

}
