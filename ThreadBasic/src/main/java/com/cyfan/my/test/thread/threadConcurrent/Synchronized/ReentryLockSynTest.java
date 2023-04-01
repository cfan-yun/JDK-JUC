package com.cyfan.my.test.thread.threadConcurrent.Synchronized;

/**
 * 可重入锁,特性及案例模拟，安全性问题探索
 * method1
 * method2
 * method3
 * 1调2,2调3,如果方法上都加了synchronized锁，无论线程启动先调用method2还是先调用method1，都会被锁住，保证顺序执行
 * 如果method1、method3 加了synchronized锁, t1调用method1 ，t2调用 method2，会导致method2没被锁住而交替执行导致COUNT值别修改，影响t1的逻辑
 *      当然method2 加上synchronized锁,但是t2在t1之前调用 method2 依然会影响t1的逻辑。这样做只是为了让count不被多加一次
 */
public class ReentryLockSynTest {

    private static int COUNT = 0;

    public static void main(String[] args) {
        ReentryLockSynTest reentryLockSynTest = new ReentryLockSynTest();
        Runnable runnable = () -> {
            //Sleep.mySleep(200);//休眠
            reentryLockSynTest.method1();


        };

        Runnable runnable2 = () -> {
            //Sleep.mySleep(100);//休眠
            reentryLockSynTest.method2();
        };
        Thread t1 = new Thread(runnable,"thread1---");
//        Thread t2 = new Thread(runnable, "thread2:::");
//        Thread t3 = new Thread(runnable,"thread3>>>");
        Thread t4 = new Thread(runnable2,"thread4|||");

//        t2.start();
//        t3.start();
        t4.start();
        t1.start();
    }

    private synchronized void method1() {
        System.out.println("CurrentThread = " + Thread.currentThread().getName() + ",method1???");
        method2();
        if (COUNT == 1){
            COUNT++;
        }
        System.out.println(Thread.currentThread().getName()+",method1???COUNT="+COUNT);
    }


    private  void method2() {
        COUNT++;
        System.out.println(Thread.currentThread().getName()+",method2...COUNT="+COUNT);
        System.out.println("CurrentThread = " + Thread.currentThread().getName() + ",method2...");
        method3();
    }

    private  synchronized void method3() {
        System.out.println("CurrentThread = " + Thread.currentThread().getName() + ",method3!!!");
    }

}
