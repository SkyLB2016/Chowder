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
public class NetworkJudgment {
    /**
     * 判断网络是否连接，推荐
     *
     * @param context context
     * @return  有链接，返回true
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
//			isAvailable()|| isConnectedOrConnecting()|| info.getState() == NetworkInfo.State.CONNECTED
            return null != info && info.isConnectedOrConnecting();
        }
        return false;
    }


    /**
     * 二、判断GPS是否打开
     *
     * @param context context
     * @return  true;false
     */
    public static boolean isGpsEnabled(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
//        LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
//        List<String> accessibleProviders = lm.getProviders(true);
//        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断基站定位是否开启，移动位置服务（LBS——Location Based Service）network
     *
     * @param context  context
     * @return   true;false
     */
    public static boolean isLBSEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * 三、判断WIFI是否打开
     *
     * @param context   context
     * @return  true;false
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) ||
                mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 四、判断是否是3G网络
     *
     * @param context  context
     * @return  true;false
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 五、判断是wifi还是手机网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。
     *
     * @param context  context
     * @return wifi=true;
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
//        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
//            return true;
//        }
//        return false;
    }
}
