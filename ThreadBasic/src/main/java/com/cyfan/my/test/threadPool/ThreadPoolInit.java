package com.cyfan.my.test.threadPool;


import java.util.concurrent.LinkedBlockingQueue;
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
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3));

        //MyTask myTask = new MyTask(1);

//        for (int i = 0; i < 10;i++){
//            threadPoolExecutor.execute(myTask);
//        }



        //boolean b = threadPoolExecutor.prestartCoreThread();//在线程池启动的时候，就提前启动一个线程
        //打印getActiveCount结果3，2，1，0均有可能， 可调小jvm内存更容易得到结果 -Xms30m -Xmx30m
        for (;;){
            int activeCount = threadPoolExecutor.getActiveCount();
            System.out.println("activeCount！！ = " + activeCount);
            int i = threadPoolExecutor.prestartAllCoreThreads();
            System.out.println(i);
            activeCount = threadPoolExecutor.getActiveCount();
            if (activeCount != 0) {
                System.out.println("activeCount = " + activeCount);
                break;
            }
            System.out.println("activeCount = " + activeCount);
        }





    }


}
