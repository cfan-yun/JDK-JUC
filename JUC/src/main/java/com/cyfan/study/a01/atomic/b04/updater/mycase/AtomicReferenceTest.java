package com.cyfan.study.a01.atomic.b04.updater.mycase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;


/**
 * AtomicReference AtomicReference 只保证对象引用的安全性,不能保证引用内部的属性安全
 */
public class AtomicReferenceTest {
    private static Account account = new Account(0);
    private static AtomicReference<Account> atomicReference = new AtomicReference<>(account);
    private static MyAtomicReferenceFieldUpdater<Account> atomicReferenceFieldUpdater = new MyAtomicReferenceFieldUpdater<>(Account.class, "money");
    private static AtomicIntegerFieldUpdater<Account> atomicIntegerFieldUpdater =  AtomicIntegerFieldUpdater.newUpdater(Account.class, "money");
    private static final int COUNT = 10000;//循环次数



    public static void main(String[] args) {
        //测试线程不安全的引用属性修改
//        testNonThreadSafeUpdateField();
        //自定义原子类实现引用属性的线程安全的修改
//        testThreadSafeUpdateField();
        //juc自带的原子类，对引用属性线程安全的修改
        testAtomicIntegerUpdater();

    }

    private static void testAtomicIntegerUpdater() {
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        for (int i = 0; i < COUNT; i++) {
            new Thread(() ->{
                plusByJUCUpdater(1);
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();//main线程卡在这里等待其他线程完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(account.getMoney());
    }

    private static void testThreadSafeUpdateField() {
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        for (int i = 0; i < COUNT; i++) {
            new Thread(() ->{
                plusByUpdater(1);
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();//main线程卡在这里等待其他线程完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(account.getMoney());
    }


    private static void testNonThreadSafeUpdateField() {
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        for (int i = 0; i < COUNT; i++) {
            new Thread(() ->{
                plus(1);
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(atomicReference.get().getMoney());
    }

    public  static void plus(int i){
        Account a = atomicReference.get();
        a.setMoney(a.getMoney() + i);
    }

    public  static void plusByUpdater(int i){
        atomicReferenceFieldUpdater.addAndGet(account, i);
    }

    public  static void plusByJUCUpdater(int i){
        atomicIntegerFieldUpdater.addAndGet(account, i);
    }



}
