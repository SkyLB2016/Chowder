package com.sky.oa.thread;

import com.sky.sdk.utils.LogUtils;

public class JoinThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " -- " + i);
            try {
                sleep(50000);
            } catch (InterruptedException e) {
                LogUtils.i("中断了吗==中断了");

                e.printStackTrace();
            }
        }
    }
}
