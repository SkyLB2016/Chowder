package com.sky.demo.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @author sky
 * @ClassName: ActivityDepManager
 * @Description: activity管理类,仅供参考不建议用了。
 * @date 2015年4月12日 下午1:33:47
 */
@Deprecated
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * @param activity 退出栈顶Activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 退出栈顶Activity,并关闭
     */
    public void popActivity() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * @return 获得当前栈顶Activity
     */
    public Activity getCurrentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    /**
     * @param activity 将当前Activity推入栈中
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity != null) popActivity(activity);
            else return;
        }
    }

    /**
     * @param cls 返回到指定的activity，杀掉在他之上的activity
     */
    public void backToAppointActivity(Class cls) {
        while (true) {
            Activity activity = getCurrentActivity();
//            if (activity == null) continue;
            if (!activity.getClass().equals(cls)) popActivity(activity);
            else return;
        }
    }
//    public void AppExit(Context context) {
//        try {
//            popAllActivity(null);
//            android.app.ActivityDepManager activityMgr = (android.app.ActivityDepManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
//        } catch (Exception e) {
//        }
//    }
}