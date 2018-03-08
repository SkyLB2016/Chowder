package com.sky.chowder.ui.activity.pend

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

import com.sky.api.IService
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.chowder.ui.service.MyService
import com.sky.utils.LogUtils

/**
 * Created by SKY on 2017/3/3.
 */
class ServiceActivity : BaseNoPActivity(), IService {

    public override fun getLayoutResId() = R.layout.activity_main

    public override fun initialize() {
        //startService();//启动服务
        bindService()//启动bind绑定服务
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    //bind 与 普通service 设定
    private var normalService = false//监听service是否启动
    private var bindService = false//监听bindservice是否启动
    private var Connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
            LogUtils.i("onServiceConnected")
            //            Message msg = new Message();
            //            msg.what = C.handler_0x001;
            //            Bundle bundle = new Bundle();
            //            bundle.putString("data", "天行健，君子以自强不息；");
            //            msg.setData(bundle);
            //            handler.sendMessage(msg);
            val binder = iBinder as MyService.MyBinder
            binder.greet("天行健，君子以自强不息；")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            LogUtils.i("onServiceDisconnected")
        }
    }

    /**
     * 在这里开启所有需要开启的服务
     */
    override fun startService() {
        normalService = true
        startService(Intent(this, MyService::class.java))
    }

    /**
     * 在这里开启所有需要开启的服务
     */
    override fun bindService() {
        bindService = true
        val service = Intent(this, MyService::class.java)
        bindService(service, Connection, Context.BIND_AUTO_CREATE)
    }

    /**
     * 在这里关闭所有需要开启的服务
     */
    override fun stopService() {
        if (bindService) {
            unbindService(Connection)
            bindService = false
        }
        if (normalService) {
            stopService(Intent(this, MyService::class.java))
            normalService = false
        }
    }


}
