package com.sky.chowder.ui.bsc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.BatteryManager;

import com.sky.chowder.C;
import com.sky.chowder.ui.service.MyService;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.ToastUtils;

/**
 * @author LiBin
 * @Description:  系统广播监听，manifest中静态注册
 * @date 2015/9/18 9:23
 */
public class SystemBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)){//开机启动
            Intent service = new Intent(context,MyService.class);
            context.startService(service);
        }else if (action.equals(Intent.ACTION_BATTERY_CHANGED)){//电量过低时
            int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);	//当前电量
            int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);		//总电量
            if (currLevel * 100 / total<100){
                ToastUtils.showShort(context, "电量过低");
            }
        }else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){//网络变化时，动态注册中为时时监控
            if (!NetworkJudgment.isConnected(context)){
                ToastUtils.showShort(context, "网络已断开broadcast");
//                Intent ac= new Intent(context, PullDownActivity.class);
//                ac.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(ac);
            }
        } else if (C.ACTION_MY.equals(action)) {//自定义广播
            ToastUtils.showShort(context, "action_broad");
        }
    }
}
