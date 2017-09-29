package com.sky.chowder.ui.bsc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.BatteryManager

import com.sky.chowder.C
import com.sky.chowder.ui.activity.PullDownActivity
import com.sky.chowder.ui.service.MyService
import com.sky.utils.NetworkUtils
import com.sky.utils.ToastUtils

/**
 * Created by SKY on 2015/9/18 9:23.
 * 系统广播监听，manifest中静态注册
 */
class SystemBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED) {//开机启动
            val service = Intent(context, MyService::class.java)
            context.startService(service)
        } else if (action == Intent.ACTION_BATTERY_CHANGED) {//电量过低时
            val currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)    //当前电量
            val total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1)        //总电量
            if (currLevel * 100 / total < 100) {
                ToastUtils.showShort(context, "电量过低")
            }
        } else if (action == ConnectivityManager.CONNECTIVITY_ACTION) {//网络变化时，动态注册中为时时监控
            if (!NetworkUtils.isConnected(context)) {
                ToastUtils.showShort(context, "网络已断开broadcast")
                val ac = Intent(context, PullDownActivity::class.java)
                ac.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(ac)
            }
        } else if (C.ACTION_MY == action) {//自定义广播
            ToastUtils.showShort(context, "action_broad")
        }
    }
}
