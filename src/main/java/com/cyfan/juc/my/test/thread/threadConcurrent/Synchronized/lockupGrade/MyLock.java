package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

/**
 * 自定义锁。
 * markWork为对象头
 */
public class MyLock {
    private MarkWork markWork;

    public MarkWork getMarkWork() {
        return markWork;
    }

    public void setMarkWork(MarkWork markWork) {
        this.markWork = markWork;
    }
}
