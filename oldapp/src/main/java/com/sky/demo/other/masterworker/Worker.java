package com.sky.demo.other.masterworker;

import java.util.Map;
import java.util.Queue;

/**
 * Created by Joker on 2015/3/9.
 */
public class Worker implements Runnable {
    //子任务队列，用于过去子任务
    protected Queue<Object> workQueue;
    //结果集
    protected Map<String, Object> resultMap;

    public void setWorkQueue(Queue<Object> workQueue) {
        this.workQueue = workQueue;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * 执行具体业务逻辑
     * 模拟立方和计算
     *
     * @param object
     * @return
     */
    public Object handle(Object object) {
        Integer i = (Integer) object;
        return i * i * i;
    }

    @Override
    public void run() {
        while (true) {
            //获取任务
            Object work = workQueue.poll();
            if (work == null)
                break;
            resultMap.put(Integer.toString(work.hashCode()), this.handle(work));
//            System.out.println("线程"+Thread.currentThread().getName()+"获得："+work);

        }
    }
}