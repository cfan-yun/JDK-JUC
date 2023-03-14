package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized;

import com.cyfan.juc.my.test.thread.uilt.Sleep;

/**
 * 可重入锁,在继承关系下是否可行？ 可行
 */
public class ReentryLockExtendsSynTest {



    public static void main(String[] args) {
        SubMySync subMySync = new SubMySync();
        Runnable runnable = () -> {
            subMySync.subMethod();
        };

        Runnable runnable2 = () -> {
            for (int i = 0; i < 5 ; i++){
                subMySync.method();
            }
        };
        Thread t1 = new Thread(runnable,"thread1---");
        Thread t2 = new Thread(runnable2, "thread2:::");

        t2.start();
        t1.start();

    }
}


class MySync{
    protected static int COUNT = 0;

    protected  void  method(){
        COUNT++;
        System.out.println(Thread.currentThread().getName()+"parent count ==="+COUNT);
    }
}


class SubMySync extends MySync{

    public synchronized void subMethod(){
        //System.out.println(Thread.currentThread().getName()+"child count ===start"+COUNT);
        while (COUNT < 10){
            COUNT++;
            Sleep.mySleep(100);
            System.out.println(Thread.currentThread().getName()+"child count ==="+COUNT);
            method();

        }
    }

}
