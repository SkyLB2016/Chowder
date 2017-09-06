package com.sky.chowder

import com.sky.Common
import com.sky.SkyApp
import com.sky.utils.LogUtils

/**
 * Created by 李彬 on 2017/3/3.
 */
class MyApplication : SkyApp() {

    override fun onCreate() {
        super.onCreate()
        Common.DEBUG = BuildConfig.DEBUG
        LogUtils.isDebug = BuildConfig.DEBUG
        //bughd监测崩溃事件
        //        FIR.init(this);
    }
}
