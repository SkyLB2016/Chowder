package com.sky.oa.thread;

public class MyThread extends Thread {
    private int ticket = 50;
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 50; i++) {
            if (this.ticket > 0) {
                System.out.println(name + "：卖票：ticket" + this.ticket--);
            }
//            try {
//                sleep(1000); //休眠1秒，避免太快导致看不到同时执行
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }
}