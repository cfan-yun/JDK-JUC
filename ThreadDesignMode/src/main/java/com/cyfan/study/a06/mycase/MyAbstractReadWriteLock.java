package com.cyfan.study.a06.mycase;

public abstract class MyAbstractReadWriteLock {
    /**
     * 读锁加锁
     */
    public abstract   void readLock() throws InterruptedException;

    /**
     * 读锁解锁
     */
    public abstract void readUnLock();

    /**
     * 写锁加锁
     */
    public abstract void writeLock() throws InterruptedException;

    /**
     * 写锁解锁
     */
    public abstract void writeUnLock();
}
