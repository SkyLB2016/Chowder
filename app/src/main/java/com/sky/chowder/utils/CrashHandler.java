package com.sky.chowder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import com.sky.chowder.MyApplication;
import com.sky.utils.DateUtil;
import com.sky.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by SKY on 2017/12/11 17:09.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    static CrashHandler crash;
    Thread.UncaughtExceptionHandler handler;
    Context context;

    public static CrashHandler getIntance() {
        if (crash == null) crash = new CrashHandler();
        return crash;
    }

    public void init(Context context) {
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        saveExceptionToSDCard(e);
        updateService();
        if (handler != null) handler.uncaughtException(t, e);
        else Process.killProcess(Process.myPid());

//        StackTraceElement[] stacks = e.getStackTrace();
//        while (stacks.length < 10) {
//            e = e.getCause();
//            stacks = e.getStackTrace();
//        }
//        LogUtils.d(e.toString());
//        printError(stacks);
    }

    /**
     * 上传到服务器
     */
    private void updateService() {


    }

    /**
     * 保存错误信息到本地
     *
     * @param e
     */
    private void saveExceptionToSDCard(Throwable e) {
        String time = DateUtil.timeStampToDate(System.currentTimeMillis(), DateUtil.YMDHMS);
        File file = new File(MyApplication.getInstance().getFileCacheDir() + "crash" + time + ".trace");
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
        } catch (IOException e1) {

        } catch (PackageManager.NameNotFoundException e1) {

        }
    }

    /**
     * 保存手机基本信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.println("App Version：" + pi.versionName + "-" + pi.versionCode);
        pw.println("OS Version：" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT);
        pw.println("Vendor：" + Build.MANUFACTURER);
        pw.println("Model：" + Build.MODEL);
        pw.println("CPU ABI：" + Build.CPU_ABI);

    }

    private void printError(StackTraceElement[] stacks) {
        for (int i = 0; i < stacks.length; i++) {
            StackTraceElement stack = stacks[i];
            String tag = "第%d个%s.%s(L:%d)";
            String className = stack.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            tag = String.format(tag, i, className, stack.getMethodName(), stack.getLineNumber());
            LogUtils.d(tag);
        }
    }
}