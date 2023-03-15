package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.myLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyLock implements Lock {

    private MySync mySync;
    public MyLock(){
        this.mySync = new MySync();
    }

    /**
     * aqs中最重要的两个属性
     * aqs =  1个队列+1个状态
     *  队列和状态，状态（可参考模拟markWord对象头）
     */
    private static class MySync extends AbstractQueuedSynchronizer{
        /**
         * 加锁逻辑
         */
        public void lock(){
            //修改成功表示拿到锁
            if (compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());// 设置当前拥有独占访问权的线程
            }else{
                acquire(1);// 设置同时只能有一个线程进入代码块
                //if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
                // 这里面需要重写tryAcquire方法，尝试自旋获取锁，没获取到锁的情况下，调用acquireQueued入队
            }
        }

        /**
         * cas 尝试获取锁，入队之前尝试获取锁，获取不到则入队
         * @param arg the acquire argument. This value is always the one
         *        passed to an acquire method, or is the value saved on entry
         *        to a condition wait.  The value is otherwise uninterpreted
         *        and can represent anything you like.
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            Thread thread = Thread.currentThread();
            int state =  getState();
            if (state == 0){
                if (compareAndSetState(0, arg)){//尝试获取锁
                    setExclusiveOwnerThread(Thread.currentThread());// 设置当前拥有独占访问权的线程
                    return true;
                }
            }else if(thread == getExclusiveOwnerThread()){//锁重入
                int next =  state + arg;
                if (next <0){
                    throw  new Error("超出锁的最大数量");
                }
                return true;
            }
            return false;
        }

        public void unLock(){
            boolean release = release(1);
            if (!release){//释放失败
                throw new RuntimeException("释放锁失败！");
            }
        }

        /**
         * cas尝试释放锁
         * @param arg the release argument. This value is always the one
         *        passed to a release method, or the current state value upon
         *        entry to a condition wait.  The value is otherwise
         *        uninterpreted and can represent anything you like.
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            int i = getState() - arg;
            if (Thread.currentThread() != getExclusiveOwnerThread()) //如果当前线程释放锁与持有锁的线程不一样，那么是非法释放锁
                throw new IllegalThreadStateException("非法释放锁！");
            boolean free = false;
            if (i == 0){
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(i);
            return free;
        }
    }

    /**
     * 加锁入口
     */
    @Override
    public void lock() {
        mySync.lock();
    }

    @Override
    public void unlock() {
        mySync.unLock();
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

    @Override
    public Condition newCondition() {
        return null;
    }
}
