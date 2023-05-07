package com.cyfan.study.a02.locks.aqs.b02.share.countdown;

public class MyCountDownLatchBySynchronized {


    private volatile int state;

    public MyCountDownLatchBySynchronized(int state) {
        this.state = state;
    }

    /**
     * 释放锁
     * state -- 操作
     * state ==  0 时唤醒排队的线程
     */
    public void countDown() {
        synchronized (this) {
            if (state <=  0) throw new IllegalArgumentException();

            state--;
            if (state == 0) {
                this.notifyAll();
            }
        }

    }

    /**
     * 获取锁
     * 循环判断state != 0 则挂起
     * @throws InterruptedException
     */
    public void await() throws InterruptedException {
        synchronized (this) {
            while (state != 0) {//不能使用if 会导致虚假唤醒
                this.wait();// 入队阻塞
                //break; 这里不需要break 因为 被 notifyAll唤醒之后，state == 0 这个循环不会再次进入
            }
        }
    }


}
