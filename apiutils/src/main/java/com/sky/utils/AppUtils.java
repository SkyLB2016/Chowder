package com.sky.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * Created by SKY on 2017/8/22.
 * app信息类
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated 不能被实例化*/
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) throws NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取应用程序版本号
     */
    public static int getVersionCode(Context context) throws NameNotFoundException {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取App包版本信息
     */
    public static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return true为已获得
     */
    public static boolean isPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查权限，未通过的自动向系统请求权限
     *
     * @param permissions 需要请求的权限数组
     * @param requestCode 权限请求成功后的请求码
     */
    public static void isPermissions(Activity activity, String[] permissions, int[] requestCode) {
        for (int i = 0; i < permissions.length; i++) {
            if (!isPermission(activity, permissions[i]))
                requestPermission(activity, new String[]{permissions[i]}, requestCode[i]);
        }
    }

    /**
     * 请求权限
     *
     * @param permissions 需要请求的权限数组
     * @param requestCode 权限请求成功后的请求码
     */
    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 下载完成后 安装App
     *
     * @param file 要安装的文件
     */
    public static void installApp(Context context, File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(install);
    }
}