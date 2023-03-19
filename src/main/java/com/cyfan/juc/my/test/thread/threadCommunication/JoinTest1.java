package com.cyfan.juc.my.test.thread.threadCommunication;

import com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.myLock.Test;
import com.cyfan.juc.my.test.thread.uilt.Sleep;

/**
 *
 * join 方法上是有synchronized锁的
 *  锁对象是调用join方法的t1线程对象
 * join的本质是调用了wait方法，wait阻塞的是是启动t1线程的的父线程
 *1.join 方法中，父线程调用了wait方法阻塞，一直等待，那么唤醒操作是再什么时候唤醒的？？？？？
 *  猜测在c++层面唤醒了父线程
 *  1.1.猜测线程结束时，会去唤醒其他线程
 *
 *
 */
public class JoinTest1 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 start....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread1 end....");
        }, "thread1");

        Thread thread2 = new Thread(() -> {

            synchronized (thread1){
                System.out.println("thread2 start....");
                try {
                    thread1.wait();//线程1卡死
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("thread2 wakeup....");
            }
        }, "thread2");

        thread1.start();
        thread2.start();
    }

}
