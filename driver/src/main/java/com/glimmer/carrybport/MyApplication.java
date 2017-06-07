package com.glimmer.carrybport;

import android.app.Application;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.sky.Common;
import com.sky.rxbus.RxBus;
import com.sky.utils.ActivityLifecycle;
import com.sky.utils.LogUtils;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

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
        SPUtils.init(this);
        LogUtils.isDebug = BuildConfig.DEBUG;
        getDataCacheDir();//缓存目录

        // 注册Activity管理器
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());
        //bughd监测崩溃事件
//        FIR.init(this);

        // 讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58f07b4b");//语音次数无限制

        //极光推送
        initJPush();

        //talkingData移动统计分析
        TCAgent.init(this.getApplicationContext(), "TD_APP_ID", "");
        TCAgent.setReportUncaughtExceptions(false);

    }

    /**
     * 退出程序
     */
    public void exit() {
        ActivityLifecycle.getInstance().popAllActivity();
    }

    /**
     * 错误提示
     *
     * @param code
     */
    public void showErroe(int code) {
        ToastUtils.showShort(getApplicationContext(), code);
    }

    /**
     * 事件总线
     *
     * @return
     */
    public RxBus getRxBus() {
        return RxBus.getInstance();
    }

    //设置极光推送
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(instance);
        JPushInterface.setSilenceTime(this, 22, 30, 8, 00);
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
