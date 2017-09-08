package com.sky.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.ParseException;
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
    public static String YM = "yyyy-MM";
    public static String YMD = "yyyy-MM-dd";
    public static String YMDHM = "yyyy-MM-dd HH:mm";
    public static String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static String CYM = "yyyy年MM月";
    public static String CYMD = "yyyy年MM月dd日";
    public static String CYMHM = "yyyy年MM月dd日HH时mm分";
    public static String CYMHMS = "yyyy年MM月dd日HH时mm分ss秒";

    /**
     * @return 当前年月日时分
     */
    public static String getCurrentTime() {
        return stampToTime(System.currentTimeMillis());
    }

    /**
     * @param context
     * @return 当前年月日（2017年8月22日）
     */
    public static String getYear(Context context) {
        return fromatDateTime(context, DateUtils.FORMAT_SHOW_YEAR);
    }

    /**
     * @param context
     * @return 当前月份 （八月）
     */
    public static String getMonth(Context context) {
        return fromatDateTime(context, DateUtils.FORMAT_NO_MONTH_DAY);
    }

    /**
     * @param context
     * @return 当前日期（8月22日）
     */
    public static String getDate(Context context) {
        return fromatDateTime(context, DateUtils.FORMAT_SHOW_DATE);
    }

    /**
     * @param context
     * @return 当前是星期几（星期二）
     */
    public static String getWeek(Context context) {
        return fromatDateTime(context, DateUtils.FORMAT_SHOW_WEEKDAY);
    }

    /**
     * @param context
     * @return 今天的时间（下午3:02）
     */
    public static String getTime(Context context) {
        return fromatDateTime(context, DateUtils.FORMAT_SHOW_TIME);
    }

    private static String fromatDateTime(Context context, int flags) {
        return DateUtils.formatDateTime(context, System.currentTimeMillis(), flags);
    }

    public static String stampToTime(long time) {
        return new SimpleDateFormat(YMDHM, Locale.CHINA).format(new Date(time));
    }

    /**
     * 时间戳转换成具体的时间
     *
     * @param time
     * @param type 索要转换成的格式
     * @return
     */
    public String stampToTime(long time, String type) {
        return new SimpleDateFormat(type, Locale.CHINA).format(new Date(time));
    }

    /**
     * 具体时间转换成时间戳
     *
     * @param time
     * @param type
     * @return
     */
    public long dateToStamp(String time, String type) {
        try {
            return new SimpleDateFormat(type, Locale.CHINA).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}