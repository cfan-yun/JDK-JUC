package com.cyfan.juc.my.test.threadPool;


import com.cyfan.juc.my.test.thread.uilt.Sleep;
import com.cyfan.juc.my.test.threadPool.policy.MyPolicy;
import com.cyfan.juc.my.test.threadPool.policy.MyTask;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池关闭
 * shutDown 和shutDownNow的区别  shutDown / stop
 *      SHUTDOWN:不会立即终止线程池，而是等到所有任务队列中的任务都执行完了才终止，但是也不再接受新任务。
 *      SHUTDOWNNOW:立即发起中断，并且会清空队列任务，而且会返回尚未执行的任务数。
 */
public class ThreadPoolClose {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2));

        threadPoolExecutor.setRejectedExecutionHandler(new MyPolicy());//5.自定义的拒绝策略--打印队列大小，线程池中活跃线程数，并开启新的线程执行

        /**
         * 线程池最大10
         *  队列最大2
         *  处理最大任务数：12
         *  超过12无空闲线程则拒绝
         *
         */
        //不可以扩大队列size
        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            threadPoolExecutor.execute(myTask);
        }


        int size = threadPoolExecutor.getQueue().size();
        int i = threadPoolExecutor.getQueue().remainingCapacity();
        System.out.println("size = " + size);
        System.out.println("i = " + i);

        //shutdown 等待线程池和队列中任务执行完成
        threadPoolExecutor.shutdown();
        Sleep.mySleep(10000);

        //shutDownNow 中断线程池中执行的线程，并清空队列
//        List<Runnable> runnableList = threadPoolExecutor.shutdownNow();
//        for (Runnable r : runnableList) {
//            System.out.println("未执行任务：====="+r);
//        }

        size = threadPoolExecutor.getQueue().size();
        i = threadPoolExecutor.getQueue().remainingCapacity();
        System.out.println("size = " + size);
        System.out.println("i = " + i);


    }
}


