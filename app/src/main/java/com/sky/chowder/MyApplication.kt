package com.sky.chowder

import com.sky.SkyApp
import com.sky.chowder.utils.CrashHandler
import com.sky.utils.LogUtils

/**
 * Created by SKY on 2017/3/3.
 */
class MyApplication : SkyApp() {

    override fun onCreate() {
        super.onCreate()
        LogUtils.isDebug = BuildConfig.DEBUG
        //bughd监测崩溃事件
        //        FIR.init(this);
        CrashHandler.getIntance().init(getInstance())
    }
}
