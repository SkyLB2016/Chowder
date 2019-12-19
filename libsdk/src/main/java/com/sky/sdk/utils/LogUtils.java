package com.sky.sdk.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * Logcat统一管理类
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，debug与release的buildconfig，也可以自定义
    private static final String TAG = "SKY";//默认情况下的前缀

    public static void setIsDebug(boolean debug) {
        isDebug = debug;
    }

    private static String generateTag() {
//        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];//此方法取得的栈的前两个分别为vm和Thread
        StackTraceElement stack = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String className = stack.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        tag = String.format(tag, className, stack.getMethodName(), stack.getLineNumber());
        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;
        return tag;
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug) Log.i(generateTag(), msg);
    }

    public static void d(String msg) {
        if (isDebug) Log.d(generateTag(), msg);
    }

    public static void e(String msg) {
        if (isDebug) Log.e(generateTag(), msg);
    }

    public static void v(String msg) {
        if (isDebug) Log.v(generateTag(), msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }
}