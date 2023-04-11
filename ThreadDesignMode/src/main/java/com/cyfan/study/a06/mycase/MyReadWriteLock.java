package com.cyfan.study.a06.mycase;

public class MyReadWriteLock extends MyAbstractReadWriteLock {

    protected int readingReaders = 0; //正在读的线程数
    protected int writingWriters = 0; //正在写的线程数
    protected boolean writeHavePriority = false; //是否写优先，在读锁退出之后，设置写优先，在写锁退出后，设置写不优先
    protected int waitingWriters = 0; //挂起中的写线程数

    @Override
    public void readLock() throws InterruptedException {
        synchronized (this) {
            while (writingWriters > 0 || (writeHavePriority && waitingWriters > 0)) {//当读线程数远大于写线程时，会导致写线程没有写的机会，拿不到锁
                // 增加读锁挂起的几率，并且写锁优先，那么就能保证机会相对均匀一点。（当wait的写线程>0 并且设置了写优先时，读线程挂起，增加读线程挂起几率）
                this.wait();//挂起的几率比写锁小
            }
            readingReaders++;
        }


    }

    @Override
    public void readUnLock() {
        synchronized (this) {
            readingReaders--;
            this.notifyAll();
            writeHavePriority = true;
        }

    }

    @Override
    public void writeLock() throws InterruptedException {
        synchronized (this) {
            waitingWriters++; //挂起的线程数
            try {
                while (readingReaders > 0 || writingWriters > 0) {
                    this.wait();//挂起的几率比写锁大
                }
            } finally {
                waitingWriters--; //挂起的线程数
            }
            writingWriters++;
        }

    }

    @Override
    public void writeUnLock() {
        synchronized (this) {
            writingWriters--;
            this.notifyAll();
            writeHavePriority = false;
        }
    }
}
