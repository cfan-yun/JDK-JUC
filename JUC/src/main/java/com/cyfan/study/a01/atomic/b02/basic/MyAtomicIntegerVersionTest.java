package com.cyfan.study.a01.atomic.b02.basic;

import java.util.concurrent.CountDownLatch;

public class MyAtomicIntegerVersionTest {

    public static void main(String[] args) {
        MyAtomicIntegerVersion myAtomicIntegerVersion = new MyAtomicIntegerVersion();
        //测试自定义原子类的线程安全性
//        testMyAtomic(myAtomicIntegerVersion);
        //模拟出现的ABA问题
        //testMyAtomicABA(myAtomicIntegerVersion);
        resolveAtomicABAByVersion(myAtomicIntegerVersion);
    }

    private static void resolveAtomicABAByVersion(MyAtomicIntegerVersion myAtomicIntegerVersion) {
        new Thread(() -> {
            try {
                int version = myAtomicIntegerVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ", version = " + version);
                Thread.sleep(5000); //休眠，这一定要比下面那个线程休眠时间短，让其先执行后续逻辑，造成ABA问题，
                boolean a = myAtomicIntegerVersion.compareAndSet(0, 1000, version, version + 1);// A -> B  0->1
                version = myAtomicIntegerVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ", aValue" +myAtomicIntegerVersion.getValue()+ ",aVersion = " + myAtomicIntegerVersion.getVersion());
                boolean b = myAtomicIntegerVersion.compareAndSet(1000, 0, version, version + 1);// B -> A 1->2
                System.out.println(Thread.currentThread().getName() + ", bValue " +myAtomicIntegerVersion.getValue() + ",bVersion = " + myAtomicIntegerVersion.getVersion());
                System.out.println(Thread.currentThread().getName() + ", a = " + a + ", b = " + b);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                int version = myAtomicIntegerVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ", version = " + version);
                Thread.sleep(8000);//休眠
                boolean compareAndSet = myAtomicIntegerVersion.compareAndSet(0, 2000, version, version + 1);// 休眠之后ABA变化之后，其无法感知到，正常修改成功
                System.out.println(Thread.currentThread().getName() + ", value = " +myAtomicIntegerVersion.getValue()+ ",version = " + myAtomicIntegerVersion.getVersion());
                System.out.println(Thread.currentThread().getName() + "是否修改成功？？？？, compareAndSet = " + compareAndSet);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();

    }

    private static void testMyAtomicABA(MyAtomicIntegerVersion myAtomicIntegerVersion) {

        new Thread(() -> {
            myAtomicIntegerVersion.compareAndSet(0, 1000); // A -> B
            myAtomicIntegerVersion.compareAndSet(1000, 0); // B -> A
            System.out.println(Thread.currentThread().getName() + ", value" +myAtomicIntegerVersion.getValue());
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                myAtomicIntegerVersion.compareAndSet(0, 2000); // 休眠之后ABA变化之后，其无法感知到，正常修改成功
                System.out.println(Thread.currentThread().getName() + ", value" +myAtomicIntegerVersion.getValue());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }

    /**
     * 测试自定义的atomic是否线程安全
     *
     * @param myAtomicIntegerVersion 原子类
     */
    private static void testMyAtomic(MyAtomicIntegerVersion myAtomicIntegerVersion) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                myAtomicIntegerVersion.incrementAndGet();
            }
            countDownLatch.countDown();
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                myAtomicIntegerVersion.incrementAndGet();

            }
            countDownLatch.countDown();
        }, "t2");

        t1.start();
        t2.start();


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(myAtomicIntegerVersion.getValue());
        System.out.println(myAtomicIntegerVersion.getVersion());
    }
}
