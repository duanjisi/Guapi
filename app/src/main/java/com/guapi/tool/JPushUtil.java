package com.guapi.tool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushUtil {

    private final String TAG = "JPushUtil";

    private final int MSG_SET_ALIAS = 1000;

    private static JPushUtil instance;
    private Context context;
    private String alias = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAlias(alias);
        }
    };
    private TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
            Log.e(TAG, "set : " + alias + " , result : " + s);
            if (i == 0 && s.equals(alias)) {
                Log.e(TAG, "set alias success : " + s);
            } else {
                Log.e(TAG, "set alias fail : " + s);
                handler.sendEmptyMessageDelayed(MSG_SET_ALIAS, 1000 * 10);
            }
        }
    };

    public static void init(Context context) {
        if (instance == null) {
            instance = new JPushUtil(context);
        }
    }

    public static JPushUtil get() {
        return instance;
    }

    private JPushUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 清除标签
     */
    public void clearAlias() {
        setAlias("");
    }

    /**
     * 设置标签
     *
     * @param alias
     */
    public void setAlias(String alias) {
        Log.e(TAG, "setting alias... : " + alias);
        this.alias = alias;
        handler.removeMessages(MSG_SET_ALIAS);

        JPushInterface.setAlias(context, alias, tagAliasCallback);
    }

}
