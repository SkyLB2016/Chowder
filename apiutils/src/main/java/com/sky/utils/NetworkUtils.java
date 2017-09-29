package com.sky.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 网络判断
 */
public class NetworkUtils {
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        return getConnect(context).getActiveNetworkInfo();
    }

    private static ConnectivityManager getConnect(Context context) {
        return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断网络是否连接，推荐
     */
    public static boolean isConnected(Context context) {
        return getActiveNetworkInfo(context).isConnected();
    }

    /**
     * 判断是否是gprs网络，即移动网络
     */
    public static boolean isMobile(Context context) {
        return getActiveNetworkInfo(context).getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断wifi是否连接
     */
    public static boolean isWifi(Context context) {
        return getActiveNetworkInfo(context).getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断GPS是否打开
     */
    public static boolean isGpsEnabled(Context context) {
        return getLocationManager(context).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断基站定位是否开启，移动位置服务
     */
    public static boolean isLBSEnabled(Context context) {
        return getLocationManager(context).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private static LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

}
