package com.cyfan.study.a02.locks.aqs.b02.share.countdown;

/**
 * main 等待多个子线程执行完之后再执行
 */
public class CountDownLatchTest {

//    private static CountDownLatch countDownLatch = new CountDownLatch(2);// 2 子线程数，表示有两个线程需要去执行countDown
//    private static MyCountDownLatch countDownLatch = new MyCountDownLatch(2);// 2 子线程数，表示有两个线程需要去执行countDown
    private static MyCountDownLatchBySynchronized countDownLatch = new MyCountDownLatchBySynchronized(2);// 2 子线程数，表示有两个线程需要去执行countDown
    public static void main(String[] args) {
        new Thread(() ->{
            try {
                System.out.println(Thread.currentThread().getName() + ", 采集cpu,ram数据，开始......");
                Thread.sleep(2000); //模拟采集
                System.out.println(Thread.currentThread().getName() + ", 采集cpu,ram数据, 结束....");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                countDownLatch.countDown();
            }

        }, "Thread1").start();

        new Thread(() ->{
            try {
                System.out.println(Thread.currentThread().getName() + ", 采集qps，io,并发数，开始......");
                Thread.sleep(1000); //模拟采集
                System.out.println(Thread.currentThread().getName() + ", 采集qps，io,并发数, 结束....");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                countDownLatch.countDown();//
            }

        }, "Thread2").start();

        System.out.println("main 线程等待线程1 线程2 采集数据...");
        try {
            countDownLatch.await();//main线程阻塞，等待子线程1，2的countDown 执行完之后才能继续
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + ", 主线程,将数据入库....");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("main线程入库完成.....");
    }
}
