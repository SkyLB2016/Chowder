package com.sky.oa.thread;

public class MyThread1 implements Runnable {
    private int ticket = 50;

    @Override
    //记得要资源公共，要在run方法之前加上synchronized关键字，要不然会出现抢资源的情况
    public synchronized void run() {
        for (int i = 0; i < 50; i++) {
            if (this.ticket > 0) {
                System.out.println("卖票：ticket" + this.ticket--);
            }
        }

    }

}

