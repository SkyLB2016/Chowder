package com.sky.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by SKY on 2015/4/12.
 * 获取空间大小
 */
public class MemoryUtils {
    /**
     * @return 手机总内存，读取手机配置文件获取
     */
    public static long getTotalBytes() {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        }
        return 0;
    }

    /**
     * @return 手机总内存
     */
    public static long getTotalBytes(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    /**
     * @return 手机剩余可用的内存
     */
    public static long getFreeBytes(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * App运行所能获取的最大内存，超过则崩溃，即OOM
     */
    public static long getAppRunMaxBytes() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * App当前运行中用到的总内存，随时变化，最多到MAX
     */
    public static long getAppRunTotalBytes() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * App运行总内存中尚未用到的部分，随时变化
     */
    public static long getAppRunFreeBytes() {
        return Runtime.getRuntime().freeMemory();
    }

//    public static void getRun(Context context) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        manager.getMemoryClass();//应用所能获取到的最大内存
//        manager.getLargeMemoryClass();//应用经过扩容后所能获取到的最大内存，一般翻倍
//    }
}