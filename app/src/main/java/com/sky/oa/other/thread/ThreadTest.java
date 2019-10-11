package com.sky.oa.other.thread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/1/8 上午9:51
 */
public class ThreadTest {
    //    public static ExecutorService executorService = Executors.newCachedThreadPool();
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Runnable runnable = new Runnable() {
             int coun = 0;

            @Override
            public void run() {
                /*执行任务*/
//                System.out.println("线程池里有" + ThreadPool.getInstance().getThreadCounter() + "个线程");
                System.out.println(++coun);
            }
        };
        for (int i = 0; i < 200000; i++) {
            executorService.execute(runnable);
//            ThreadPool.getInstance().execute(runnable);
        }
    }
}
