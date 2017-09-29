package com.sky.chowder.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import com.sky.base.BaseNoPActivity
import com.sky.chowder.C
import com.sky.chowder.R
import com.sky.utils.NetworkUtils
import com.sky.utils.ToastUtils

/**
 * Created by SKY on 2017/3/3.
 */

class BroadActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.tv

    override fun onResume() {
        super.onResume()
        // 动态注册reciver
        val filter = IntentFilter()
        filter.priority = 1000//动态注册顺序高于静态注册
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)//网络变化监听
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)//电量变化监听
        filter.addAction(Intent.ACTION_BATTERY_LOW)//电量过低监听
        filter.addAction(C.ACTION_PUSH_DATA)//自定义的action
        filter.addAction(C.ACTION_NEW_VERSION)//自定义的action
        filter.addAction(C.ACTION_MY)//自定义的action
        registerReceiver(receiver, filter)
        //还可能发送统计数据，比如第三方的SDK 做统计需求
        send()//发送广播
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)//解注册
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    //broadcast广播
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                ConnectivityManager.CONNECTIVITY_ACTION ->// 网络发生变化
                    if (!NetworkUtils.isConnected(context)) ToastUtils.showShort(context, "网络已断开")
                Intent.ACTION_BATTERY_CHANGED -> {
                    val currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)    //当前电量
                    val total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1)        //总电量
                    if (currLevel * 100 / total < 10) ToastUtils.showShort(context, "电量过低")
                }
                C.ACTION_PUSH_DATA -> { // 可能有新数据
                    val b = intent.extras
                    b.get("data")
                }
                C.ACTION_NEW_VERSION -> {
                    // 可能发现新版本
                    // VersionDialog 可能是版本提示是否需要下载的对话框
                }
                C.ACTION_MY -> {
                    showToast("actionmy")
                    abortBroadcast()//终止传递
                }
            }
        }
    }

    private fun send() {
        val broad = Intent(C.ACTION_MY)
        broad.putExtra("msg", "baseactivity")
        //        sendBroadcast(broad);
        sendOrderedBroadcast(broad, "sky.permission.ACTION_MY")
    }
}