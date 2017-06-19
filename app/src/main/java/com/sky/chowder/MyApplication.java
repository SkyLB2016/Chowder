package com.sky.chowder;

import android.app.Application;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.rxbus.RxBus;
import com.sky.utils.ActivityLifecycle;
import com.sky.utils.LogUtils;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;

import java.io.File;

/**
 * Created by 李彬 on 2017/3/3.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    private String cacheDir;//缓存目录 /data/user/0/com.glimmer.carrybport/cache
    private String fileCacheDir;//文件图片日志缓存文件夹目录 /data/user/0/com.glimmer.carrybport/files

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Common.DEBUG = BuildConfig.DEBUG;
        SPUtils.init(this);
        LogUtils.isDebug = BuildConfig.DEBUG;
        getDataCacheDir();//缓存目录

        // 注册Activity管理器
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
        //bughd监测崩溃事件
//        FIR.init(this);
    }

    /**
     * 事件总线
     *
     * @return
     */
    public RxBus getRxBus() {
        return RxBus.getInstance();
    }

    /**
     * 获取缓存文件夹
     */
    private void getDataCacheDir() {
        cacheDir = getCacheDir().getAbsolutePath();
        fileCacheDir = getFilesDir().getAbsolutePath();
    }

    /**
     * 获取图片缓存目录
     *
     * @return 图片缓存目录
     */
    public String getCashDir() {
        return cacheDir + File.separator;
    }

    /**
     * 获取文件缓存目录
     *
     * @return 文件缓存目录
     */
    public String getFileCacheDir() {
        return fileCacheDir + File.separator;
    }

    /**
     * 退出程序
     */
    public void exit() {
        ActivityLifecycle.getInstance().popAllActivity();
    }

    public void showToast(int code) {
        ToastUtils.showShort(getApplicationContext(), code);
    }

    public void showToast(String text) {
        ToastUtils.showShort(getApplicationContext(), text);
    }

    public <T> T getObject(String text, T value) {
        return (T) SPUtils.getInstance().get(text, value);
    }

    public <T> void setObject(String text, T value) {
        SPUtils.getInstance().put(text, value);
    }

    public boolean getUsertOnline() {
        return !TextUtils.isEmpty(getObject(Common.TOKEN, ""));
    }


}
