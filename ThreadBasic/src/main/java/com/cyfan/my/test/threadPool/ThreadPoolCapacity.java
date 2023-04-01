package com.cyfan.my.test.threadPool;


import com.cyfan.my.test.threadPool.policy.MyPolicy;
import com.cyfan.my.test.threadPool.task.MyTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池容量
 *
 *  容量限制设置：
 *      动态调整线程数和队列容量
 *       动态调整线程数可以通过设置threadPoolExecutor.setMaximumPoolSize(13)设置增多线程
 *       如何动态调整队列容量？？？？？
 *          不能修改的原因是，LinkedBlockingQueue中容量是final修饰的，方案是重写LinkedBlockingQueue源码
 *
 *       案例：将线程池放到不同的机器（线程池1[192.168.xx.x1]、线程池2[192.168.xx.x2]....线程池n）
 *
 *       思考：其他框架的线程池容量是如何调整的？？？
 *              tomcat里面容量是如何调整的？？
 *                  先把核心线程数扩容为最大线程数，然后再往队列里面塞任务。
 *                  而jdk中的策略是达到核心线程数，先入对队列，队满之后才会扩容到最大线程数。
 */
public class ThreadPoolCapacity {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2));

        threadPoolExecutor.setRejectedExecutionHandler(new MyPolicy());//5.自定义的拒绝策略--打印队列大小，线程池中活跃线程数，并开启新的线程执行

        /**
         * 线程池最大10
         *  队列最大2
         *  处理最大任务数：12
         *  超过12无空闲线程则拒绝
         *
         */
        threadPoolExecutor.setMaximumPoolSize(13);//调整最大现线程数才管用13 + 2可处理15个任务
        //不可以扩大队列size
        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            threadPoolExecutor.execute(myTask);
        }
    }
}


