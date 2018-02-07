package com.sky;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.sky.utils.ActivityLifecycle;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;

import java.io.File;

/**
 * Created by SKY on 2017/3/3.
 */
public class SkyApp extends Application {
    protected static SkyApp instance;

    private String cacheDir;//缓存目录 /storage/emulated/0/Android/data/包名/cache
    private String fileCacheDir;//文件日志缓存文件夹目录 /storage/emulated/0/Android/data/包名/files/Documents
    private String picCacheDir;//图片文件夹目录 /storage/emulated/0/Android/data/包名/files/Pictures

    public static SkyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SPUtils.init(this);
        getDataCacheDir();//缓存目录
        registerActivityLifecycleCallbacks(ActivityLifecycle.getInstance());//注册Activity管理器
    }

    /**
     * 获取缓存文件夹
     */
    private void getDataCacheDir() {
        cacheDir = getExternalCacheDir().getAbsolutePath() + File.separator;
        fileCacheDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + File.separator;
        picCacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
    }

    /**
     * 获取图片缓存目录
     *
     * @return 图片缓存目录
     */
    public String getCashDir() {
        return cacheDir;
    }


    /**
     * 获取文件缓存目录
     *
     * @return 文件缓存目录
     */
    public String getFileCacheDir() {
        return fileCacheDir;
    }

    /**
     * 图片缓存目录
     *
     * @return
     */
    public String getPicCacheDir() {
        return picCacheDir;
    }

    /**
     * 退出程序
     */
    public void exit() {
        ActivityLifecycle.getInstance().closeAll();
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
