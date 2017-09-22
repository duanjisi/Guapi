package com.ewuapp.framework.common.utils;

import android.text.TextUtils;

import com.ewuapp.framework.common.BaseApplication;

/**
 * @author Jewel
 * @version 1.0
 * @since 2016/9/7 0007
 */

public class StringFormat {

    public static String formatForRes(int resID, String value) {
        return String.format(BaseApplication.getInstance().getResources().getString(resID), value);
    }

    public static String formatForRes(int resID) {
        return BaseApplication.getInstance().getString(resID);
    }
    public static String formatForRes( int resID, Object... value) {
        return String.format(BaseApplication.getInstance().getResources().getString(resID), value);
    }

    public static String appendString(String... data) {
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            if (!TextUtils.isEmpty(s)) {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * 前缀拼接、空数据替换
     *
     * @param appendSplit 拼接前缀
     * @param emptyString 空数据替换
     * @param data        原始数据
     * @return
     */
    public static String appendString(String appendSplit, String emptyString, String[] data) {
        StringBuilder sb = new StringBuilder();
        String format = null;
        for (String s : data) {
            if (!TextUtils.isEmpty(s)) {
                format = s;
            } else {
                format = emptyString;
            }
            if (sb.length() == 0) {
                sb.append(format);
            } else {
                sb.append(appendSplit).append(format);
            }
        }
        return sb.toString();
    }


    public static String appendStringForRes(int... resIDs) {
        StringBuilder sb = new StringBuilder();
        for (int resID : resIDs) {
            sb.append(BaseApplication.getInstance().getString(resID));
        }
        return sb.toString();
    }

    /**
     * 对比数组，判断是否含有target字段
     * @param target  目标字段
     * @param targets 源字段
     * @return boolean
     */
    public static boolean includeTarget(String target, String... targets) {
        boolean include = false;
        for(String temp : targets) {
            if(TextUtils.equals(target, temp)) {
                include  = true;
            }
        }
        return include;
    }
}
