package com.cyfan.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

/**
 * ObjectMonitor 锁住的线程封装为ObjectWaiter对象，入cxq队列
 */
public class ObjectWaiter {
    private Thread thread;


    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
