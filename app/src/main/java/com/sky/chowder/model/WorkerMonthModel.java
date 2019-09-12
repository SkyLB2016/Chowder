package com.sky.chowder.model;

/**
 * Created by libin on 2019/08/30 19:27 Friday.
 */
public class WorkerMonthModel {
    private String date;
    private int worker;//在职
    private int entryWorker;//入职
    private int quitWorker;//离职
    private int toWorker;//转正

    public WorkerMonthModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWorker() {
        return worker;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }

    public int getEntryWorker() {
        return entryWorker;
    }

    public void setEntryWorker(int entryWorker) {
        this.entryWorker = entryWorker;
    }

    public int getQuitWorker() {
        return quitWorker;
    }

    public void setQuitWorker(int quitWorker) {
        this.quitWorker = quitWorker;
    }

    public int getToWorker() {
        return toWorker;
    }

    public void setToWorker(int toWorker) {
        this.toWorker = toWorker;
    }
}
