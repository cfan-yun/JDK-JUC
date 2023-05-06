package com.cyfan.study.a02.locks.aqs.b02.share.semaphore;

import com.cyfan.study.a02.locks.aqs.MyAbstractQueuedSynchronizer;

public class MySemaphore {

    private final MySync sync;


    public MySemaphore(int permits) {
        this.sync = new MyNonFairSync(permits);
    }

    public MySemaphore(int permits, boolean isFair) {
        this.sync = isFair ? new MyFairSync(permits) : new MyNonFairSync(permits);
    }


    static abstract class MySync extends MyAbstractQueuedSynchronizer {
        MySync(int permits) {
            setState(permits);
        }

        public int getPermits() {
            return getState();
        }

        /**
         * 共享锁释放
         * state + 1
         *
         * @param arg 释放数量
         * @return false 释放失败
         */
        @Override
        public boolean tryReleaseShared(int arg) {
            for (; ; ) {//可能多线程释放锁
                int current = getState();
                int next = current + arg;
                if (next < current){//说明arg是负数
                    throw  new Error("参数异常");
                }
                if (compareAndSwapState(current,next)){//改失败了继续循环,成功说明释状态加成功
                    return true;
                }
                return false;
            }

        }
    }


    /**
     * 公平锁
     */
    static class MyFairSync extends MySync {
        MyFairSync(int permits) {
            super(permits);
        }


        /**
         * 公平 锁尝试获取锁
         *
         * @param permits 令牌，或者锁数量
         * @return 所剩的令牌数
         */
        @Override
        protected int tryAcquireShared(int permits) {
            for (; ; ) {//自旋操作，这里是和独占锁有区别的地方
                //检查队列
                if (hasQueuePrev()) {//检查阻塞队列是否有线程在等待，如果有则直接返回
                    return -1;
                }
                int state = getState();
                int i = state - permits;
                if (i < 0 ||
                        compareAndSwapState(state, i)) { // 修改成功  i >= 0
                    return i;
                }
            }
        }
    }

    /**
     * 非公平锁
     */
    static class MyNonFairSync extends MySync {

        MyNonFairSync(int permits) {
            super(permits);
        }

        /**
         * 非公平锁尝试获取锁
         *
         * @param permits
         * @return
         */
        @Override
        protected int tryAcquireShared(int permits) { //预备10人拿锁,state 初始值= 10
            for (; ; ) {//自旋操作，这里是和独占锁有区别的地方。所线程操作，没有拿到锁的线程可以继续拿锁（锁还有的剩）
                int state = getState();
                int i = state - permits;
                if (i < 0 ||
                        compareAndSwapState(state, i)) { // 修改成功  i >= 0
                    return i;// i = 0 （第十个人进来拿锁，可以拿到）, < 0 (第是一个人进来拿锁，拿不到), > 0 (前面9个人拿锁，可以拿到)
                }
            }
        }
    }

    /**
     * 共享获取锁 （响应中断的）
     *
     * @param permits 锁/令牌数量
     * @throws InterruptedException 中断异常
     */
    public void acquire(int permits) throws InterruptedException {
        sync.acquireSharedInterruptibly(permits);
    }

    /**
     * 共享获取锁 （响应中断的）
     *
     * @throws InterruptedException 中断异常
     */
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * 共享获取锁（不响应中断）
     *
     * @param permits 锁/令牌数量
     */
    public void acquireUnInterruptibly(int permits) {
        sync.acquireShared(permits);
    }


    /**
     * 释放锁
     * @return 释放锁是否成功
     */
    public boolean release() {
        return sync.releaseShared(1);//释放锁
    }
}
