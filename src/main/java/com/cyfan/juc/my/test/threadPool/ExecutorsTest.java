package com.cyfan.juc.my.test.threadPool;

import java.util.concurrent.*;

/**
 * Executors框架阿里不推荐使用
 *  主要原因：最大线程和最大队列数没有设置（Integer.MAX_VALUE 作为最大线程数或队列容量）容易引起OOM异常
 */
public class ExecutorsTest {

    public static void main(String[] args) {

        //1.推荐使用方式
        new ThreadPoolExecutor(3, 10,
                0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(5));

        //2.不推荐使用方式  Executors.newFixedThreadPool
        Executors.newFixedThreadPool(10);//LinkedBlockingQueue 是无界队列没有给容量，任务排队增多容易OOM

        //3.不推荐使用方式 Executors.newSingleThreadExecutor
        Executors.newSingleThreadExecutor();//LinkedBlockingQueue是无界队列没有给容量，任务排队增多容易OOM

        //4.不推荐使用方式 Executors.newCachedThreadPool
        Executors.newCachedThreadPool();//最大线程数使用：Integer.MAX_VALUE， 队列使用SynchronousQueue，线程增多容易OOM

        //5.不推荐使用方式Executors.newScheduledThreadPool
        Executors.newScheduledThreadPool(1); //最大线程数使用：Integer.MAX_VALUE，队列使用DelayedWorkQueue线程增多容易OOM

    }
}
