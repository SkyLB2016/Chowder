package com.sky.chowder.ui.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

import com.sky.utils.LogUtils


/**
 * Created by 李彬 on 2017/3/3.
 * 写成守护线程的服务
 */
class MyService : Service() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.i("oncreate")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        LogUtils.i("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        LogUtils.i("onBind")
        return MyBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        LogUtils.i("onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.i("onDestroy")
    }

    inner class MyBinder : Binder() {
        fun greet(msg: String) {
            LogUtils.i(msg + "地势坤，君子以厚德载物。")
        }
    }
}