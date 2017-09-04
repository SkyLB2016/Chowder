package com.sky.demo.other.masterworker;

/**
 * Created by Joker on 2015/3/9.
 */
public class RunTest implements Runnable {
    int count = 10;

    @Override
    public void run() {
        while (true) {
            //获取任务
            if (count > 0) count--;
            else return;
            System.out.println(Thread.currentThread().getName()+"剩余"+count);

        }
    }
}