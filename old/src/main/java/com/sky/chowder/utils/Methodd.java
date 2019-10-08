package com.sky.chowder.utils;

import android.util.Log;

/**
 * Created by SKY on 2018/8/23 上午10:58.
 */
public class Methodd {
    private static String generateTag() {
//        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];//此方法取得的栈的前两个分别为vm和Thread
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            StackTraceElement stack = stacks[i];
            String tag = "%s.%s(L:%d)";
            String className = stack.getClassName();
//            className = className.substring(className.lastIndexOf(".") + 1);
            Log.i("name","name==" + String.format(tag, className, stack.getMethodName(), stack.getLineNumber()));
        }
//        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;
        return "";
    }
}
