package com.cyfan.juc.my.test.threadPool.policy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义的策略
 *
 */
public class MyPolicy implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if(!executor.isShutdown()){//线程池未关闭
            System.out.println("!!!"+Thread.currentThread().getName()+",队列大小="+executor.getQueue().size());
            System.out.println("!!!"+Thread.currentThread().getName()+",活动线程数="+executor.getActiveCount());
            new Thread(r).start();
        }
    }
}
