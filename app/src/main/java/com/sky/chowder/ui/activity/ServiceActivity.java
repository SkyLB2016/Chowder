package com.sky.chowder.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.sky.api.IService;
import com.sky.chowder.C;
import com.sky.chowder.ui.BaseActivity;
import com.sky.utils.LogUtils;

/**
 * Created by 李彬 on 2017/3/3.
 */

public class ServiceActivity extends BaseActivity implements IService {

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initialize() {
        //startService();//启动服务
        bindService();//启动bind绑定服务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    //bind 与 普通service 设定
    private boolean normalService = false;//监听service是否启动
    private boolean bindService = false;//监听bindservice是否启动
    ServiceConnection Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i("onServiceConnected");
            Message msg = new Message();
            msg.what = C.handler_0x001;
            Bundle bundle = new Bundle();
            bundle.putString("data", "天行健，君子以自强不息；");
            msg.setData(bundle);
            handler.sendMessage(msg);
            MyService.MyBinder binder = (MyService.MyBinder) service;
            binder.greet("天行健，君子以自强不息；");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i("onServiceDisconnected");
        }
    };

    /**
     * 在这里开启所有需要开启的服务
     */
    @Override
    public void startService() {
        normalService = true;
        startService(new Intent(this, MyService.class));
    }

    /**
     * 在这里开启所有需要开启的服务
     */
    @Override
    public void bindService() {
        bindService = true;
        Intent service = new Intent(this, MyService.class);
        bindService(service, Connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 在这里关闭所有需要开启的服务
     */
    @Override
    public void stopService() {
        if (bindService) {
            unbindService(Connection);
            bindService = false;
        }
        if (normalService) {
            stopService(new Intent(this, MyService.class));
            normalService = false;
        }
    }


}
