package com.cyfan.study.a02.locks.aqs.b02.share.semaphore;

public class MySemaphoreBySynchronized {

    private volatile int state;

    public MySemaphoreBySynchronized(int state) {
        this.state = state;
    }

    /**
     * 获取令牌
     * state -- 操作
     * state ==  0 时，代表没有锁了，需要阻塞
     */
    public void acquire() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (state > 0) {//这里表示有锁可拿
                    state--;
                    break;
                }
                //走到这里说名没有锁可拿
                this.wait();
                //唤醒之后继续判断是否有锁可拿，如果有，则继续唤醒后继节点
                if (this.state > 0) {//这一段可有可无
                    this.notifyAll();
                }
            }
        }

    }

    /**
     * state ++ 操作
     */
    public void release() {
        synchronized (this) {
            state++;
            this.notify(); // 有锁了唤醒抢锁
        }
    }
}
