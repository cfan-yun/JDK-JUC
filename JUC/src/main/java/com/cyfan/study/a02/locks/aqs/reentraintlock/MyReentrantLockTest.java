package com.cyfan.study.a02.locks.aqs.reentraintlock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1、测试自定义重入锁正确性
 * 2、测试公平锁和非公平锁是否正确
 * *   公平锁：按照入队顺序执行
 * *   非公平锁：不按照入队顺序执行
 * *    5个线程，公平锁应该顺序执行，非公平锁，可能会插队
 */
public class MyReentrantLockTest {

    private static final MyReentrantLock lock = new MyReentrantLock(true);

    public static void main(String[] args) {
        //测试自定义重入锁 MyReentrantLock 可用性
        testReentrantLock();
        //验证公平锁非公平锁，公平：按入队顺序执行，非公平：不按入队顺序执行
        testReentrantLockFair();
    }


    private static void testReentrantLock() {
        new Thread(() -> twoLock()).start();
        new Thread(() -> twoLock()).start();
    }

    private static void testReentrantLockFair() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> twoLock()).start();
        }
    }



    private static void oneLock() {
        lock.lock(); // 这里要写在try的外面。写在try的里面会有问题
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(Thread.currentThread().getName() + " oneLock 进入...." + LocalDateTime.now().format(formatter));
            twoLock();
            System.out.println(Thread.currentThread().getName() + " oneLock 退出" + LocalDateTime.now().format(formatter));
        } finally {
            lock.unlock();
        }
    }

    private static void twoLock() {
        System.out.println(Thread.currentThread().getName() + "准备进入锁！");
        lock.lock();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(Thread.currentThread().getName() + " twoLock 进入...." + LocalDateTime.now().format(formatter));
            TimeUnit.SECONDS.sleep(2); //休眠
            System.out.println(Thread.currentThread().getName() + " twoLock 退出" + LocalDateTime.now().format(formatter));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(Thread.currentThread().getName() + "退出方法");
            lock.unlock();
        }

    }
}
