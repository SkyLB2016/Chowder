package com.sky.chowder.ui;

import android.app.Application;

import com.sky.chowder.BuildConfig;
import com.sky.utils.ActivityLifecycle;
import com.sky.utils.LogUtils;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;

import im.fir.sdk.FIR;

/**
 * Created by 李彬 on 2017/3/3.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
//    private LockPatternUtils mLockPatternUtils;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //bughd
        FIR.init(this);
        SPUtils.init(this);
        instance = this;
        LogUtils.isDebug = BuildConfig.DEBUG;
        // 初始化自定义Activity管理器
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
    }

    public void exit() {
        ActivityLifecycle.getInstance().popAllActivity();
    }

    public void showErroe(int code) {
        ToastUtils.showError(getApplicationContext(), code);
    }
}
