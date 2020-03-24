package com.sky.oa;

import android.app.Application;

import com.sky.sdk.utils.ActivityLifecycle;
import com.sky.sdk.utils.LogUtils;
import com.sky.sdk.utils.SPUtils;
import com.sky.sdk.utils.ToastUtils;

/**
 * Created by SKY on 2017/3/3.
 */
public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SPUtils.init(this);
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());//注册Activity管理器
        LogUtils.setIsDebug(BuildConfig.DEBUG);
        //bughd监测崩溃事件
        //        FIR.init(this);
//        CrashHandler.intance.init(getInstance())

    }

    /**
     * 退出程序
     */
    public void exit() {
        ActivityLifecycle.getInstance().closeAll();
    }
    public void showToast(String text) {
        ToastUtils.showLong(this,text);
    }


}
