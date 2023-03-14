package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

public class LockRecord {

    private MarkWork markWork;//markWord的拷贝 即c中head
    private MarkWork owner;


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
