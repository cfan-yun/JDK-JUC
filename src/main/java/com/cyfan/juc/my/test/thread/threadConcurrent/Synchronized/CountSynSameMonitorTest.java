package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized;

import com.cyfan.juc.my.test.thread.uilt.Sleep;

/**
 * synchronized 作用于成员方法，锁对象是this
 * synchronized 作用于静态方法，锁对象是class这个类本身的字节码文件
 *
 * synchronized(obj) 中的obj是同一对象则是同一把锁，不同对象，是不同的锁
 *
 *
 */
public class CountSynSameMonitorTest {

     static Object object = new Object();

    public static void main(String[] args) {
        CountSynSameMonitorTest countSynSameMonitorTest = new CountSynSameMonitorTest();
        CountSynSameMonitorTest countSynSameMonitorTest2 = new CountSynSameMonitorTest();

        Runnable runnable =  () -> {
            String name = Thread.currentThread().getName();
            if(name != null && "thread1".equals(name)){
                    countSynSameMonitorTest.isOK();
            }else{
                    countSynSameMonitorTest2.isNoOK();
            }

        };

        Thread t1 = new Thread(runnable,"thread1");
        Thread t2 = new Thread(runnable,"thread2");
        Thread t3 = new Thread(runnable,"thread3");

        t1.start();
        t2.start();
        t3.start();
    }

    public synchronized void isOK(){//this 锁 不同对象就不是同一把锁
        System.out.println("this is OK!");
        Sleep.mySleep(3000);
        System.out.println("time is out");
    }


    public synchronized void isNoOK(){//this 锁
        System.out.println("currentThread = "+Thread.currentThread().getName()+ ", this is No OK!");
    }

}
