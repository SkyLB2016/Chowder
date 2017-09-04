package com.sky.demo.model;

import java.io.Serializable;
/**
 * Created by sky on 15/12/9 下午8:54.
 */
public class ProcessEntity implements Serializable {

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
