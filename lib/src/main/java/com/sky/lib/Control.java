package com.sky.lib;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicInteger;

public class Control {
    public static void main(String[] args) {
//        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
//        for (ThreadInfo threadInfo : threadInfos) {
//            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
//        }

        new LockThread(true).start();
        new LockThread(false).start();
    }
}

class LockThread extends Thread {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    boolean flag;

    public LockThread(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (LockThread.lock1) {
                    System.out.println("lock1位置1");

                    synchronized (LockThread.lock2) {
                        System.out.println("lock2位置1");
                    }

                }
            }
        } else {
            while (true) {
                synchronized (LockThread.lock2) {
                    System.out.println("lock2位置2");
                    synchronized (LockThread.lock1) {
                        System.out.println("lock1位置2");
                    }

                }
            }
        }
    }
}