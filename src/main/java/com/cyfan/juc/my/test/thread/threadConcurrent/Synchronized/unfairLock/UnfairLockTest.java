package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.unfairLock;

/**
 * 非公平锁测试
 *  非公平：先来的线程不一定获取到锁，先执行
 * <p>
 *  测试：创建10个线程，将其按顺序启动，并且阻塞在同一个位置，
 *        然后再让他们一起开始抢锁，如果线程是按顺序执行，那么是公平锁，线程不是按顺序执行，那么是非公平锁
 *        1.如何保证10个线程阻塞在同一个位置？
 *            main线程种启动10个线程的时候，先获取到 另外10个线程需要抢的同一把锁，那么10个线程启动后，都会阻塞
 *        2.如何保证10个线程按顺序启动，for循环启动时，每启动一个线程则休眠一段时间，保证顺序
 *        3.执行结果：1-9先启动，但是获取锁的顺序是9-1，？？？为什么？？
 * <p>
 * 原理：三大队列
 *       cxq、entryList、waitSet
 *              entryList产生的原因是减小cxq的压力， 没有 entryList 情况下，线程挂起和唤醒都需要对cxq的头结点进行cas修改，
 *              cxq头结点压力极大，因此引入entryList, 唤醒时，不在cxq唤醒，在entryList中唤醒，唤醒的顺序有几种模式，
 *              默认顺序表现为从cxq的头结点唤醒。
 *       cxq（cxq是栈类型）中线程启动后抢锁，没抢到锁，挂起是从头部插入，需要重新抢锁时，唤醒也是从头部唤醒，
 *           栈先进后出，所以启动顺序1-9 ，获取锁的顺序9-1
 */
class UnfairLockTest {
    private static final Object OBJ = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                synchronized (OBJ){
                    System.out.println(Thread.currentThread().getName() +", 获得了锁！");
                }
            }, "thread"+i);
        }

        //这里main先获取到锁，那么就能保证10个线程都阻塞在这里
        synchronized (OBJ){
            for (Thread thread : threads) {
                thread.start();
                System.out.println(thread.getName() + ", 启动了！");
                Thread.sleep(500);
            }
        }

        /**
         * thread0, 启动了！
         * thread1, 启动了！
         * thread2, 启动了！
         * thread3, 启动了！
         * thread4, 启动了！
         * thread5, 启动了！
         * thread6, 启动了！
         * thread7, 启动了！
         * thread8, 启动了！
         * thread9, 启动了！
         * thread9, 获得了锁！
         * thread8, 获得了锁！
         * thread7, 获得了锁！
         * thread6, 获得了锁！
         * thread5, 获得了锁！
         * thread4, 获得了锁！
         * thread3, 获得了锁！
         * thread2, 获得了锁！
         * thread1, 获得了锁！
         * thread0, 获得了锁！
         */

    }
}
