package com.cyfan.study.a02.locks.aqs.b02.share.countdown;

import com.cyfan.study.a02.locks.aqs.MyAbstractQueuedSynchronizer;

public class MyCountDownLatch {

    private final MySync sync;

    public MyCountDownLatch(int count) {
        if (count < 0)
            throw new IllegalArgumentException();

        this.sync = new MySync(count);
    }


    static class MySync extends MyAbstractQueuedSynchronizer {

        MySync(int count) {
            setState(count);
        }

        public int getCount() {
            return getState();
        }

        /**
         * CountDownLatch释放
         * state -1
         *
         * @param arg 需要减去的数量
         * @return false 释放失败
         */
        @Override
        public boolean tryReleaseShared(int arg) {
            for (; ; ) {//自旋表示，可能有多个线程同时执行countDown操作
                int current = getState();
                if (current == 0) return false; //等于0 说明不需要释放了，之前线程已经释放了


                int next = current - arg;
                if (next > current) {//说明arg是负数
                    throw new Error("参数异常");
                }
                if (compareAndSwapState(current, next)) {//改失败了继续循环,成功说明释状态加成功
                    return next == 0;// state - 1 == 0 才需要去释放锁
                }
                return false;
            }

        }

        /**
         * countDownLatch 判断是否获取到锁， state ==  0 就表示能够获取到锁，
         * @param acquires 锁数量
         * @return
         */
        @Override
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;//为doAcquireSharedInterruptibly 中的   int i = tryAcquireShared(arg); 这里服务。1 表示需要去获取锁
        }
    }

    /**
     * countDownLatch获取锁 （响应中断的）
     *
     * @throws InterruptedException 中断异常
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * 释放锁
     * @return 释放锁是否成功
     */
    public boolean countDown() {
        return sync.releaseShared(1);//释放锁
    }


}
