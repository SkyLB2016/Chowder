package com.sky.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.sky.R;

import java.io.Serializable;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * activity跳转
 */
public class JumpAct {
    /**
     * 跳转activity，并定义跳转动画
     */
    public static void jumpActivity(Context context, Class<?> cls) {
        jumpActivity(context, new Intent(context, cls));
    }

    public static void jumpActivity(Context context, Class<?> cls, String name, CharSequence value) {
        jumpActivity(context, new Intent(context, cls).putExtra(name, value));
    }

    public static void jumpActivity(Context context, Class<?> cls, String name, String entity) {
        jumpActivity(context, new Intent(context, cls).putExtra(name, entity));
    }

    public static void jumpActivity(Context context, Class<?> cls, String name, Serializable entity) {
        jumpActivity(context, new Intent(context, cls).putExtra(name, entity));
    }

    /**
     * @param className 绝对路径
     */
    public static void jumpActivity(Context context, String className) {
        jumpActivity(context, new Intent().setClassName(context, className));
    }

    /**
     * 与setClassName 一样
     *
     * @param className 绝对路径
     */
    public static void jumpActivity(String className, Context context) {
        ComponentName component = new ComponentName(context, className);
        jumpActivity(context, new Intent().setComponent(component));
    }

    /**
     * @param packageName 包名
     * @param className   相对路径
     */
    public static void jumpActivity(Context context, String packageName, String className) {
        jumpActivity(context, new Intent().setClassName(packageName, className));
    }

    public static void jumpActivity(Context context, Class<?> cls, String... values) {
        Intent intent = new Intent(context, cls);
        String name = null;
        String value = null;
        for (int i = 0; i < values.length; i++) {
            if (i % 2 == 0) name = values[i];
            else if (i % 2 == 1) value = values[i];
            intent.putExtra(name, value);
        }
        jumpActivity(context, intent);
    }

    public static void jumpActivity(Context context, Intent intent) {
        //高德有问题
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((Activity) context)).
//                    toBundle());
//        else {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}