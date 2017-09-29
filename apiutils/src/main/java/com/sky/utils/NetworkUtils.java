package com.sky.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 网络判断
 */
public class NetworkUtils {
    /**
     * 判断网络是否连接，推荐
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivity.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    /**
     * 二、判断GPS是否打开
     */
    public static boolean isGpsEnabled(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断基站定位是否开启，移动位置服务
     */
    public static boolean isLBSEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * 三、判断WIFI是否打开 ，打开为true
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return connect.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED ||
                tele.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS;
    }

    /**
     * 四、判断是否是3G网络 true为是
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 五、判断是wifi还是手机网络， wifi=true;
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
