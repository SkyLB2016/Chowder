package com.sky.chowder.utils;

import com.sky.utils.LogUtils;

/**
 * Created by SKY on 2017/12/11 17:09.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtils.i(e.getMessage());
        LogUtils.i(e.getLocalizedMessage());
        LogUtils.i(e.fillInStackTrace()+"");
        LogUtils.i(e.getCause().toString());
        LogUtils.i(e.getStackTrace().toString());
        LogUtils.i(e.getSuppressed().toString());
    }
}
