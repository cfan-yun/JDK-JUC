package com.cyfan.juc.my.test.thread.threadCommunication;

import com.cyfan.juc.my.test.thread.uilt.Sleep;

/**
 *
 * join 方法上是有synchronized锁的
 *  锁对象是调用join方法的t1线程对象
 * join的本质是调用了wait方法，wait阻塞的是是启动t1线程的的父线程
 *1.join 方法中，父线程调用了wait方法阻塞，一直等待，那么唤醒操作是再什么时候唤醒的？？？？？
 *      JoinTest1 解答
 *      Thread.join ->Object.wait -> object.c -> JVM_MonitorWait -> jvm.cpp -> JVM_MonitorWait ->  ObjectSynchronizer::wait
 *      -> ObjectSynchronizer::inflate ->　 monitor->wait　-> waitSet队列
 *
 *
 *
 */
public class JoinTest {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()+", 执行开始!");
        Thread thread1= new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+", 执行开始!");
            Sleep.mySleep(1000);
            System.out.println(Thread.currentThread().getName()+", 执行完成!");
        }, "thread-1");


        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+", 执行开始!");
            Sleep.mySleep(1000);
            System.out.println();
            System.out.println(Thread.currentThread().getName()+", 执行完成!");
        }, "thread-2");

        thread1.start();//thread1 和main同步执行
        thread2.start();//thread2 、thread1 和main同步执行

        try {
            //谁调用thread1.join(), 则告诉的线程就是谁
            thread1.join();//t1 告诉main，你先等等，等我跑完
            thread2.join();//t2 告诉main， 你先等等，等我跑完
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+", main执行完成!");
    }

}
