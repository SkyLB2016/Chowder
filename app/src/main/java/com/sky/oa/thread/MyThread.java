package com.sky.oa.thread;

public class MyThread extends Thread {
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + ":" + i);
            try {
                sleep(1000); //休眠1秒，避免太快导致看不到同时执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}