package com.sky.demo.ui.bsc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sky.demo.utils.LogUtils;

/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/18 9:43
 */
public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy");
    }

    public class MyBinder extends Binder {
        public void greet(String msg) {
            LogUtils.i(msg+"地势坤，君子以厚德载物。");
        }
    }
}
