package com.sky.chowder.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;

import com.sky.base.BaseNoPActivity;
import com.sky.chowder.C;
import com.sky.chowder.R;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.ToastUtils;

/**
 * Created by 李彬 on 2017/3/3.
 */

public class ReceiverActivity extends BaseNoPActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 动态注册reciver
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);//动态注册顺序高于静态注册
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络变化监听
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);//电量变化监听
        filter.addAction(Intent.ACTION_BATTERY_LOW);//电量过低监听
        filter.addAction(C.ACTION_PUSH_DATA);//自定义的action
        filter.addAction(C.ACTION_NEW_VERSION);//自定义的action
        filter.addAction(C.ACTION_MY);//自定义的action
        registerReceiver(receiver, filter);
        //还可能发送统计数据，比如第三方的SDK 做统计需求
        send();//发送广播
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);//解注册
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    //broadcast广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) { // 网络发生变化
                if (!NetworkJudgment.isConnected(context))
                    ToastUtils.showShort(context, "网络已断开");
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);    //当前电量
                int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);        //总电量
                if (currLevel * 100 / total < 10) {
                    ToastUtils.showShort(context, "电量过低");
                }
            } else if (C.ACTION_PUSH_DATA.equals(action)) { // 可能有新数据
                Bundle b = intent.getExtras();
                b.get("data");
            } else if (C.ACTION_NEW_VERSION.equals(action)) { // 可能发现新版本
//                 VersionDialog 可能是版本提示是否需要下载的对话框
            } else if (C.ACTION_MY.equals(action)) {
                showToast("actionmy");
                abortBroadcast();//终止传递
            }
//            String msg = intent.getStringExtra("msg");
//            Bundle bundle = new Bundle();
//            bundle.putString("msg", msg + "@FirstReceiver");
//            setResultExtras(bundle);//传递给下一个广播
        }
    };

    public void send() {
        Intent broad = new Intent(C.ACTION_MY);
        broad.putExtra("msg", "baseactivity");
//        sendBroadcast(broad);
        sendOrderedBroadcast(broad, "sky.permission.ACTION_MY");
    }

}
