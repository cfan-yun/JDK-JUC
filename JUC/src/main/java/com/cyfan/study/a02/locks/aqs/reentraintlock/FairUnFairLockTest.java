package com.cyfan.study.a02.locks.aqs.reentraintlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1、测试自定义重入锁正确性
 * 2、测试公平锁和非公平锁是否正确
 * *   公平锁：按照入队顺序执行
 * *   非公平锁：不按照入队顺序执行
 * *    5个线程，每个线程进行两次加锁，公平锁应该按入队顺序执行，不存在两次一起执行的情况，非公平锁，可能会插队
 */
public class FairUnFairLockTest {

    private static final MyReentrantLock unFairLock = new MyReentrantLock(false);//  非公平锁
    private static final MyReentrantLock fairLock = new MyReentrantLock(true); // 公平锁
    private static final ReentrantLock unFairLock1 = new ReentrantLock(false);
    public static void main(String[] args) {
        runLock(unFairLock);


    }

    private static void runLock(Lock lock) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Task(lock)) {
                @Override
                public String toString() {
                    return getName();
                }

            };
            thread.setName(i + "");
            thread.start();
        }
    }


    static class Task implements Runnable {

        private final Lock lock;

        Task(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    System.out.println("当前获得锁的线程[" + Thread.currentThread().getName() + "] , 队列 " + ((MyReentrantLock) lock).getReverseQueuedThreads());
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
