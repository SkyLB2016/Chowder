package com.sky.oa.other.masterworker;

import java.io.IOException;
import java.util.Map;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/1/7 下午4:30
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Test().executeCubic();
//        new Test().for3();
//        RunTest test =new RunTest();
//        for (int i = 0; i < 4; i++) {
//            Thread thread = new Thread(test,Integer.toString(i));
//            thread.start();
//        }
    }

    /**
     * 计算立方和1~100的立方和
     */
    public void executeCubic() {
        //创建三个Worker线程，执行任务
        Master master = new Master(3);

        for (int i = 0; i < 100; i++) {
            master.submit(i);
        }

        //执行任务
        master.execute();
        //计算结果，初始化
        int result = 0;

        //计算结果集
        Map<String, Object> resultMap = master.getResultMap();

        while (resultMap.size() > 0 || !master.isComplete()) {

            String key = null;
            Integer i = null;

            for (String k : resultMap.keySet()) {

                key = k;
                break;
            }

            if (key != null) {
                i = (Integer) resultMap.get(key);
                //从结果集中移除倍计算过的key
                resultMap.remove(key);
            }

            if (i != null) {
                result += i;
            }
        }
        System.out.println(result);
        System.out.println(Runtime.getRuntime().availableProcessors());

    }

    public void for3() {
        long start = System.nanoTime();
        int result = 0;
        for (int i = 0; i < 100; i++) {
            result += i * i * i;
            System.out.println(result);

        }
        System.out.println(result);
        System.out.println(System.nanoTime() - start);


    }
}
