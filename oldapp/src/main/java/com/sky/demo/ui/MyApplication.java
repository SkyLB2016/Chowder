package com.sky.demo.ui;

import android.app.Application;

import com.sky.demo.BuildConfig;
import com.sky.utils.ActivityLifecycle;
import com.sky.utils.LogUtils;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;
import com.squareup.leakcanary.LeakCanary;

import org.xutils.x;

import im.fir.sdk.FIR;


/**
 * @author sky QQ:1136096189
 * @Description: 基础MyApplication
 * @date 15/11/28 下午12:38
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
        FIR.init(this);
        LeakCanary.install(this);
        instance = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        SPUtils.init(this);
        LogUtils.isDebug = BuildConfig.DEBUG;
//        mLockPatternUtils = new LockPatternUtils(this);

        // 初始化自定义Activity管理器
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
    }

    public void exit() {
        ActivityLifecycle.getInstance().popAllActivity();
    }

    public void showErroe(int code) {
        ToastUtils.showShort(getApplicationContext(), code);
    }
}
