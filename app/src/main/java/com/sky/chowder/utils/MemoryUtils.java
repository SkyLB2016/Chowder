package com.sky.chowder.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemoryUtils  {

    List<ProcessEntity> processEntities;

    public void getRunningAppProcessInfo(Context context) {
        processEntities = new ArrayList<>();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appRunProList = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo pro : appRunProList) {
            // 进程ID号
            int pid = pro.pid;
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            int uid = pro.uid;
            // 进程名，默认是包名或者由属性android：process=""指定
            String processName = pro.processName;
            // 获得该进程占用的内存
            int[] myMempid = new int[]{pid};
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            Debug.MemoryInfo[] memoryInfo = manager.getProcessMemoryInfo(myMempid);
            // 获取进程占内存用信息 kb单位
            int memSize = memoryInfo[0].dalvikPrivateDirty;

            String appName = "";
            try {
                ApplicationInfo appinfo = context.getPackageManager().getApplicationInfo(processName, 0);
                appName = (String) appinfo.loadLabel(context.getPackageManager());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            // 构造一个ProcessInfo对象
            ProcessEntity processInfo = new ProcessEntity();
            processInfo.setPid(pid);
            processInfo.setUid(uid);
            processInfo.setMemSize(memSize);
            processInfo.setProcessName(processName);
            processInfo.setAppName(appName);
            processEntities.add(processInfo);

            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
//            String[] packageList = pro.pkgList;
//            LogUtils.i("process id is " + pid + "has " + packageList.length);
//            for (String pkg : packageList) {
//                LogUtils.i("packageName " + pkg + " in process id is -->" + pid);
//            }
        }
    }
}
class ProcessEntity implements Serializable {

    private int uid;//进程id，Android规定android.system.uid=1000
    private int pid;//进程所在的用户id ，即该进程是有谁启动的 root/普通用户等
    private int memSize;//进程占用的内存大小,单位为kb
    private String processName;//进程名，包名
    private String appName;//进程名，包名

    public ProcessEntity() {

    }
    public ProcessEntity(int uid, int pid, int memSize, String processName) {
        this.uid = uid;
        this.pid = pid;
        this.memSize = memSize;
        this.processName = processName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
