package com.cyfan.juc.my.test.threadPool.policy;

public class MyTask implements Runnable{

    int count = 0;

    public MyTask(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", start.....繁忙");
        //count++;
        if (Thread.currentThread().isInterrupted()){//当前线程发生中断
            System.out.println(Thread.currentThread().getName() +", 发生中断=============");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() +", 发生中断=============");
        }
        System.out.println(Thread.currentThread().getName() + ", end.....空闲");

    }
}
