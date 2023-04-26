package com.cyfan.study.a02.locks.aqs.b01.execlusive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        //测试 synchronized 的锁重入
//        testSynchronized();
        //测试 ReentrantLock 的锁重入
        testReentrantLock();
    }

    private static void testSynchronized() {
        new Thread(() -> one(), "t1").start();
        new Thread(() -> two(),"t2").start();
    }

    private synchronized static void one() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(Thread.currentThread().getName() +" one 进入...."+ LocalDateTime.now().format(formatter));
        two();
        System.out.println(Thread.currentThread().getName() + " one 退出" + LocalDateTime.now().format(formatter));
    }

    private synchronized static void two() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(Thread.currentThread().getName() +" two 进入...."+ LocalDateTime.now().format(formatter));
        try {
            TimeUnit.SECONDS.sleep(5); //休眠5秒
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()  +" tow 退出"+ LocalDateTime.now().format(formatter));
    }


    private static void testReentrantLock() {
        new Thread(() -> oneLock()).start();
        new Thread(() -> twoLock()).start();
    }


    private static void oneLock() {
        lock.lock(); // 这里要写在try的外面。写在try的里面会有问题
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(Thread.currentThread().getName()  +" oneLock 进入...."+ LocalDateTime.now().format(formatter));
            twoLock();
            System.out.println(Thread.currentThread().getName()  +" oneLock 退出"+ LocalDateTime.now().format(formatter));
        } finally {
            lock.unlock();
        }
    }

    private static void twoLock() {
        lock.lock();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(Thread.currentThread().getName()  +" twoLock 进入...."+ LocalDateTime.now().format(formatter));
            TimeUnit.SECONDS.sleep(5); //休眠5秒
            System.out.println(Thread.currentThread().getName()  +" twoLock 退出"+ LocalDateTime.now().format(formatter));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }

    }
}
