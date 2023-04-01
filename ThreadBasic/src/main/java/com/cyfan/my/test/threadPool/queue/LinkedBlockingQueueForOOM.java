package com.cyfan.my.test.threadPool.queue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 模拟使用无界队列造成的内存泄露
 */
public class LinkedBlockingQueueForOOM {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        //模拟使用无界队列导致OOM
        for (int i = 0; i < Integer.MAX_VALUE; i++) {


            MyTask myTask = new MyTask(i);
            threadPoolExecutor.execute(myTask);

            //打印队列中线程数
            if (threadPoolExecutor.getQueue().size() % 10000 == 0){
                System.out.println(threadPoolExecutor.getQueue().size());
            }
        }

    }



}

class MyTask implements Runnable{

    int count = 0;

    public MyTask(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", start.....繁忙");
        //Long[] longs = new Long[1024 * 1024];//1KB
        //count++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + ", end.....空闲");

    }
}
