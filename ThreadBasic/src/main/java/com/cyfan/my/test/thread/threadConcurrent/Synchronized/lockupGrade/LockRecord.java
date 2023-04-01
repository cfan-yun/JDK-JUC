package com.cyfan.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

/**
 * 轻量级锁，在线程栈中表示锁记录的指针
 */
public class LockRecord {

    private MarkWork markWork;//markWord的拷贝 即c中
    private MarkWork owner; // 指向markWord 原值


    public MarkWork getMarkWork() {
        return markWork;
    }

    public void setMarkWork(MarkWork markWork) {
        this.markWork = markWork;
    }

    public MarkWork getOwner() {
        return owner;
    }

    public void setOwner(MarkWork owner) {
        this.owner = owner;
    }
}
