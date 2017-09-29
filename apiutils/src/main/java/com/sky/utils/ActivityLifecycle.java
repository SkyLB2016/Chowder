package com.sky.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * activity管理类
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private Stack<Activity> activityStack;
    private Activity currentActivity;
    private static ActivityLifecycle instance;


    public static ActivityLifecycle getInstance() {
        if (instance == null)
            //所有加上synchronized 和 块语句，在多线程访问的时候，同一时刻只能有一个线程能够用
            //同步检查，获得锁，先清空工作内存，即子内存；然后从主内存中拷贝变量的新副本到子内存中，
            // 执行后强制刷新主内存，并释放
            synchronized (ActivityLifecycle.class) {
                if (instance == null)
                    instance = new ActivityLifecycle();
            }
        return instance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activityStack == null) activityStack = new Stack<>();
        activityStack.add(activity);//堆入activitymanager管理栈中
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityStack.remove(activity);//销毁时从管理栈中移除
        if (currentActivity == activity) currentActivity = null;
    }

    public Activity getCurrentAct() {
        return currentActivity;
    }

    /**
     * @return 当前activity的位置
     */
    public int getCurrentPosition() {
        return activityStack.size() - 1;
    }

    /**
     * @param position 所要获取的activity的位置
     * @return 获取指定的activity
     */
    public Class<? extends Activity> getAppointPosition(int position) {
        return activityStack.get(position).getClass();
    }

    /**
     * 移除所有的activity
     */
    public void popAllActivity() {
        while (!activityStack.isEmpty()) {
            activityStack.lastElement().finish();
            activityStack.remove(activityStack.lastElement());
        }
    }

    /**
     * 返回到指定的activity
     */
    public void backToAppointActivity(Class cls) {
        while (!activityStack.lastElement().getClass().equals(cls)) {
            activityStack.lastElement().finish();
            activityStack.remove(activityStack.lastElement());
        }
    }

    /**
     * 杀掉并移除 出去当前activity的所有页面
     */
    public void keepCurrentActivity() {
        while (activityStack.size() != 1) {
            activityStack.firstElement().finish();
            activityStack.remove(activityStack.firstElement());
        }
    }
}
