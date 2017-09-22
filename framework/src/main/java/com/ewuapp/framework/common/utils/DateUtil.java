package com.ewuapp.framework.common.utils;


import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作工具类
 *
 * @author jewelbao
 * @version 1.0
 * @since 2016/9/1
 */
public class DateUtil {

	private static final String TAG = "DateUtil";
	// 特殊日子专用
	static String endTime = "2016-11-11 23:59:59";
	public static final String startTime = "2016-11-02 00:00:00";


	public static String getStringTime(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}


	/**
	 * 是否处在特殊时间内
	 *
	 * @param time 要判断的时间
	 * @return 是返回true
	 */
	public static boolean isOnSpecialTime(long time) {
		String parseTime = getStringTime(time);
		if(isBefore(startTime, parseTime) && isBefore(parseTime, endTime)) {
			return true;
		}
		return false;
	}


	/**
	 * 之前时间和当前时间的差值小于几天
	 * @param time          给定时间
	 * @param currentTime   当前时间
	 * @param day           几天
	 * @return
	 */
	public static boolean isInAfterDay(long time,long currentTime ,int day){
		return currentTime - time < 1000L * day*24 * 60 * 60 ;

	}

	/**
	 * yyyy-MM-dd HH:mm:ss的字符串的时间格式转为long毫秒级的时间格式
	 *
	 * @param time 时间参数 格式：1990-01-01 12:00:00
	 * @return long 毫秒级的时间格式
	 */
	public static long getLongTime(String time) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(time);
			return date.getTime();
		} catch(Exception e) {
			Log.e(TAG, e.toString());
		}
		return -1;
	}

	/**
     * 将String转换自定义格式字符串
     *
     * @param time       未格式化的是时间格式
     * @param formatType 自定义格式  yyyy-MM-dd HH:mm:ss
     */
    public static String getStringTime(String time, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(getLongTime(time));
    }

    /**
     * 将String转换自定义格式字符串
     *
     * @param time       未格式化的是时间格式
     * @param formatType 自定义格式  yyyy-MM-dd HH:mm:ss
     */
    public static long getLongTime(String time, String formatType) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatType);
            Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (Exception e) {
			Log.e(TAG, e.toString());
        }
        return -1;
    }

	/**
	 * 将long转换为yyyy-MM-dd HH:mm:ss字符串
	 *
	 * @param time 毫秒级的时间格式
	 * @return String yyyy-MM-dd HH:mm:ss字符串
	 */
	public static String getStringTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

	/**
	 * 将String转换为yyyy-MM-dd HH:mm:ss字符串
	 *
	 * @param time 未格式化的是时间格式
	 * @return String yyyy-MM-dd HH:mm:ss字符串
	 */
	public static String getStringTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 *
	 * @param start 时间参数 1 格式：1990-01-01 12:00:00
	 * @param end   时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static Object[] getDistanceTimes(String start, String end) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = dateFormat.parse(start);
			two = dateFormat.parse(end);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if(time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch(ParseException e) {
			Log.e(TAG, e.toString());
		}
		return new Object[]{day, hour, min, sec};
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 *
	 * @param start 时间参数 1 格式：1990-01-01 12:00:00
	 * @param end   时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String start, String end) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = dateFormat.parse(start);
			two = dateFormat.parse(end);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if(time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch(ParseException e) {
			Log.e(TAG, e.toString());
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	/**
	 * 根据时间start和end，判断start是否小于end
	 *
	 * @param start 时间参数 1 格式：1990-01-01 12:00:00
	 * @param end   时间参数 2 格式：2009-01-01 12:00:00
	 * @return boolean 如果start大于end，返回false
	 */
	public static boolean isBefore(String start, String end) {
		if(TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
			throw new RuntimeException("param 'start' or param 'end' is null");
		}
		boolean result = true;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = dateFormat.parse(start);
			Date endDate = dateFormat.parse(end);

			if(startDate.after(endDate)) {
				result = false;
			}
		} catch(Exception e) {
			Log.e(TAG, e.toString());
		}
		return result;
	}


    public static String getArticleTime(String updateDate, String serviceDate) {
        if(TextUtils.isEmpty(updateDate)) {
            return "";
        }

        String hm = getStringTime(updateDate, "HH:mm");
        String mdhm = getStringTime(updateDate, "MM-dd HH:mm");
        String articleTime;

        if(TextUtils.isEmpty(serviceDate)) {
            return  mdhm;
        }

        long serviceDayStart = getLongTime(serviceDate, "yyyy-MM-dd");
        long update = getLongTime(updateDate);
        long day = 24 * 60 * 60 * 1000;

        if (serviceDayStart <= update && update <= getLongTime(serviceDate)) {
            articleTime = "今天 " + hm;
        } else if (serviceDayStart - day <= update && update < serviceDayStart) {
            articleTime = "昨天 " + hm;
        } else if (serviceDayStart - 2 * day<= update && update < serviceDayStart) {
            articleTime = "前天 " + hm;
        } else {
            articleTime = mdhm;
        }

        return articleTime;
    }
}