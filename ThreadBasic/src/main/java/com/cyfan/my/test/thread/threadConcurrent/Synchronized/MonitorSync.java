package com.cyfan.my.test.thread.threadConcurrent.Synchronized;

import com.cyfan.my.test.thread.uilt.Sleep;

/**
 * 使用jvisualvm打开，可以观察到，
 *          t1-Running  locked
 *          t2-Blocked  waiting for lock
 *          t3-Blocked  waiting for lock
 */
public class MonitorSync {

    private static Object OBJ =  new Object();

    public static void main(String[] args) {
        Runnable run = () ->{
            synchronized (OBJ){
                Sleep.mySleep(100000);
            }
        };

        new Thread(run,"t1").start();
        new Thread(run,"t2").start();
        new Thread(run,"t3").start();
    }
}
