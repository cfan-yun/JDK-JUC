package com.cyfan.study.a02.locks.aqs.b01.execlusive.reentraintlock;

import com.cyfan.study.a02.locks.aqs.MyAbstractQueuedSynchronizer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReentrantLock implements Lock {

    /**
     * MySync -> MyAbstractQueuedSynchronizer
     */
    abstract static class MySync extends MyAbstractQueuedSynchronizer {
        abstract void lock();

        /**
         * 释放锁，公平锁和非公平锁公用的释放锁逻辑
         *
         * @param arg 锁重入次数
         * @return true 释放锁成功
         */
        @Override
        protected boolean tryRelease(int arg) {
            int i = getState() - arg;
            if (Thread.currentThread() != getOwnerThread()) {//当前线程和持有锁的线程不同，那么是非法释放锁！
                throw new IllegalMonitorStateException("非法释放锁！");
            }

            boolean free = false;

            if (i == 0) {
                free = true;
                setOwnerThread(null);//清空   owner字段
            }
            setState(i); // 走到这里表示重入的 （state -1 != 0）
            return free;
        }

    }

    static class MyFairSync extends MySync {
        /**
         * 公平锁加锁入口
         */
        @Override
        void lock() {
            acquire(1);// 参数1有两个功能  a. cas 0->1(首次加锁) b.代表重入一次(state + 1)
        }

        /**
         * 尝试快速获取锁
         * *  1.尝试将state cas操作改为1
         * *  2.将　owner　引用指向当前线程
         *
         * @return true 获取锁成功
         */
        @Override
        protected boolean tryAcquire(int arg) {

            Thread currentThread = Thread.currentThread();//获取到当前线程
            int state = getState();//获取状态
            //当前还没有线程获取到锁，所以，所有线程一起抢锁
            if (state == 0) {
                //1.检查队列
                //2.没有排队，cas 修改state状态
                //当前没有人排队才进行cas操作
                if (!hasQueuePrev() //hasQueuePrev 返回false表示没有在排队线程,队列为空或者当前线程为头结点的后继
                        && compareAndSwapState(0, arg)) {
                    System.out.println(Thread.currentThread().getName() + ", 快速尝试获取锁成功");
                    setOwnerThread(currentThread);
                    return true;
                }
            } else if (currentThread == getOwnerThread()) { // 判断当前线程是否是持有锁的线程(重入逻辑)
                int newState = state + arg;
                if (newState < 0) {
                    throw new Error("数据异常");
                }
                super.setState(newState);// 锁重入
                return true;
            }
            //获取锁失败
            System.out.println(Thread.currentThread().getName() + ", 快速尝试获取锁失败!!");
            return false;

        }
    }

    static class MyNonFairSync extends MySync {

        /**
         * 非公平锁加锁入口
         */
        @Override
        void lock() {
            //上来就cas操作，成功就设置状态为1 ，表示获取到锁。（非公平性体现在这里）
            if (compareAndSwapState(0, 1)) {
                setOwnerThread(Thread.currentThread());
            } else {
                acquire(1);
            }
        }


        /**
         * 非公平锁，尝试快速获取锁
         *
         * @param arg 锁重入次数
         * @return true 快速尝试获取锁成功
         */
        @Override
        public boolean tryAcquire(int arg) {
            Thread currentThread = Thread.currentThread();//获取到当前线程
            int state = super.getState();//获取状态
            //当前还没有线程获取到锁，所以，所有线程一起抢锁
            if (state == 0) {
                //1.cas 修改state状态
                if (super.compareAndSwapState(0, arg)) {
                    super.setOwnerThread(currentThread);
                    return true;
                }
            } else if (currentThread == getOwnerThread()) { // 判断当前线程是否是持有锁的线程(重入逻辑)
                int newState = state + arg;
                if (newState < 0) {
                    throw new Error("数据异常");
                }
                super.setState(newState);// 锁重入
                return true;
            }
            //获取锁失败
            return false;
        }
    }


    private final MySync sync;

    public MyReentrantLock() {
        this.sync = new MyNonFairSync();
    }

    public MyReentrantLock(boolean fair) {
        this.sync = fair ? new MyFairSync() : new MyNonFairSync();
    }


    /**
     * 加锁
     */
    @Override
    public void lock() {
        //公平锁加锁
        //非公平锁加锁
//        int i =  1/0;//模拟报错,lock.lock()在try里面执行，那么这里抛出的算数异常会被吞噬掉
        this.sync.lock();//根据构造函数决定的是公平还是非公平锁

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        //公平非公平锁逻辑是一样的
        //1.state(1,0),owner = null;
        //2.唤醒head的后继节点
        //释放锁不需要区分公平锁还是非公平锁。
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public List<Thread> getQueuedThreads(){
        return sync.getQueuedThreads();
    }

    public List<Thread> getReverseQueuedThreads(){
        List<Thread> queuedThreads = sync.getQueuedThreads();
        Collections.reverse(queuedThreads);
        return queuedThreads;
    }
}
