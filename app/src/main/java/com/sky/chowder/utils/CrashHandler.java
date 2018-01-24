package com.sky.chowder.utils;

import android.content.Context;

import com.sky.utils.LogUtils;

/**
 * Created by SKY on 2017/12/11 17:09.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    static CrashHandler crash;

    public static CrashHandler getIntance() {
        if (crash == null) crash = new CrashHandler();
        return crash;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtils.i("错误产生类所在方法==" + e.getStackTrace()[0].getMethodName());
        LogUtils.i("错误所在行数==" + e.getStackTrace()[0].getLineNumber());
        LogUtils.i("错误产生类==" + e.getStackTrace()[0].getFileName());
        LogUtils.i("错误产生类所在路径==" + e.getStackTrace()[0].getClassName());
        LogUtils.i("错误类型==" + e.getMessage());
        System.exit(1);
    }
}
