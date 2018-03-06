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
        StackTraceElement[] stacks = e.getStackTrace();
        while (stacks.length < 10) {
            e = e.getCause();
            stacks = e.getStackTrace();
        }
        LogUtils.d(e.toString());
        printError(stacks);
        System.exit(1);
    }

    private void printError(StackTraceElement[] stacks) {
        for (int i = 0; i < stacks.length; i++) {
            StackTraceElement stack = stacks[i];
            String tag = "第%d个%s.%s(L:%d)";
            String className = stack.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            tag = String.format(tag, i, className, stack.getMethodName(), stack.getLineNumber());
            LogUtils.d(tag);
        }
    }
}