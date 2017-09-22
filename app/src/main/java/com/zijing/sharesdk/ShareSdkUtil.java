package com.zijing.sharesdk;

import android.content.Context;

/**
 * Created by longbh on 16/6/30.
 */
public class ShareSdkUtil {

    public static void share(Context context, ShareCallBack callBack) {
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setShareCall(callBack);
        shareDialog.show();
    }
}
