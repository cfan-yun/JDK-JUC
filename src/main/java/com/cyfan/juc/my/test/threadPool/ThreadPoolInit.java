package com.cyfan.juc.my.test.threadPool;

import com.cyfan.juc.my.test.threadPool.policy.MyTask;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池初始化
 *  线程池启动时，提前启动一个线程prestartCoreThread，提前启动所有核心线程prestartAllCoreThreads
 *
 */
public class ThreadPoolInit {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3));

        //MyTask myTask = new MyTask(1);

//        for (int i = 0; i < 10;i++){
//            threadPoolExecutor.execute(myTask);
//        }



        //boolean b = threadPoolExecutor.prestartCoreThread();//在线程池启动的时候，就提前启动一个线程
        int i = threadPoolExecutor.prestartAllCoreThreads();
        System.out.println(i);
        int activeCount = threadPoolExecutor.getActiveCount();
        System.out.println("activeCount = " + activeCount);




    }


}
