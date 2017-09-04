package com.sky.demo.other.masterworker;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Joker on 2015/3/9.
 */
public class Master {
    //任务队列
    protected Queue<Object> workQueue = new ConcurrentLinkedQueue<Object>();
    //线程队列
    protected Map<String, Thread> threadMap = new HashMap<String, Thread>();
    //子任务处理结果集
    protected Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();

    public Master(int workerCount) {
        Worker worker = new Worker();
        worker.setWorkQueue(workQueue);
        worker.setResultMap(resultMap);
        if (workerCount > 5)
            workerCount = 5;
     /*根据workerCount，创建指定数量的工作线程Worker*/
        for (int i = 0; i < workerCount; i++) {
            threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
        }
    }

    /**
     * 检查子任务是否全部执行完毕
     */
    public boolean isComplete() {
        for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
            if (entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 提交任务
     */
    public void submit(Object object) {
        workQueue.add(object);
    }

    /**
     * 获取子任务结果集
     *
     * @return 结果集
     */
    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    /**
     * 执行所有工作线程worker，处理任务。
     */
    public void execute() {
        for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
            entry.getValue().start();
        }
    }
}