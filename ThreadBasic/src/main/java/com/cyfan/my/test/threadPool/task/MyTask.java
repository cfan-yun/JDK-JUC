package com.cyfan.my.test.threadPool.task;

public class MyTask implements Runnable{

    int num = 0;

    public MyTask(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() +"——>"+ num +"号线程, start.....繁忙");
        //count++;
        if (Thread.currentThread().isInterrupted()){//当前线程发生中断
            System.out.println(Thread.currentThread().getName()+"——>"+ num +"号线程 发生中断=============");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() +"——>"+ num +"号线程 发生中断=============");
        }
        System.out.println(Thread.currentThread().getName() +"——>"+ num +"号线程 end.....空闲");

    }
}
