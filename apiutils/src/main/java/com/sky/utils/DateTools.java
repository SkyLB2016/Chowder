package com.sky.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//        LogUtils.i("7==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_NO_MONTH_DAY));        //八月
//        LogUtils.i("10==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_NUMERIC_DATE));        //8/22
//        LogUtils.i("11==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE));           //8月22日
//        LogUtils.i("12==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));           //下午3:02
//        LogUtils.i("13==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_WEEKDAY));        //星期二
//        LogUtils.i("14==" + DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR));           //2017年8月22日

/**
 * Created by SKY on 2017/8/22.
 * 日期工具
 */
public class DateTools {
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * @return 当前年月日时分
     */
    public static String getCurrentTime() {
        return formatTime(System.currentTimeMillis());
    }

    /**
     * @param context
     * @return 当前年月日（2017年8月22日）
     */
    public static String getYear(Context context) {
        return FromatDateTime(context, DateUtils.FORMAT_SHOW_YEAR);
    }

    /**
     * @param context
     * @return 当前月份 （八月）
     */
    public static String getMonth(Context context) {
        return FromatDateTime(context, DateUtils.FORMAT_NO_MONTH_DAY);
    }

    /**
     * @param context
     * @return 当前日期（8月22日）
     */
    public static String getDate(Context context) {
        return FromatDateTime(context, DateUtils.FORMAT_SHOW_DATE);
    }

    /**
     * @param context
     * @return 当前是星期几（星期二）
     */
    public static String getWeek(Context context) {
        return FromatDateTime(context, DateUtils.FORMAT_SHOW_WEEKDAY);
    }

    /**
     * @param context
     * @return 今天的时间（下午3:02）
     */
    public static String getTime(Context context) {
        return FromatDateTime(context, DateUtils.FORMAT_SHOW_TIME);
    }

    public static String formatTime(long time) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM, Locale.CHINA).format(new Date(time));
    }

    private static String FromatDateTime(Context context, int flags) {
        return DateUtils.formatDateTime(context, System.currentTimeMillis(), flags);
    }
}