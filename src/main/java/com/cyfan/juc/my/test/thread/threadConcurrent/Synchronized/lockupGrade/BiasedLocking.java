package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class BiasedLocking {

    public boolean revokeAndRebias(MyLock myLock) {
        MarkWork markWork = myLock.getMarkWork();
        long threadID = markWork.getThreadID();

        //获取偏向锁
        String biasedLock = markWork.getBiasedLock();//获取偏向锁标记
        String lockFlag = markWork.getFlag();//获取锁标记
        Unsafe unsafe = MyUnsafe.getUnsafe();
        Field threadIDField = null;
        try {
            threadIDField = markWork.getClass().getDeclaredField("threadID");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        long offset = unsafe.objectFieldOffset(threadIDField);
        long currentThreadID = unsafe.getLongVolatile(markWork, offset);//原值
        Thread thread = Thread.currentThread();
        long id = thread.getId();//当前线程ID
        //可偏向但是还没有偏向任何线程
        if (threadID == -1L && (biasedLock != null && "1".equals(biasedLock)) && (lockFlag != null && "01".equals(lockFlag))) {//说明还没有偏向
            boolean isOK = unsafe.compareAndSwapLong(markWork, offset, currentThreadID, id);
            if (isOK) {//争抢成功
                return true;
            }
        }
        //可偏向，并且已经偏向某个线程
        else if (threadID != -1L && (biasedLock != null && "1".equals(biasedLock)) && (lockFlag != null && "01".equals(lockFlag))) {
            if (threadID == id) {//判断是否是偏向当前线程
                return true;
            } else {//如果线程ID不一致，撤销偏向锁
                //撤销偏向锁，撤销完成，这里需要判断很多东西，在Java层面不一定能实现，判断线程是否执行完同步代码块
                revokeBiased(myLock);
                return false;
            }
        }

        return false;
    }

    /**
     * 撤销偏向锁
     * 最准确的判断是否已经离开同步代码块
     * 松一点判断线程是否存活
     */
    private boolean revokeBiased(MyLock myLock) {
        //判断是否已经离开同步代码块(松一点判断线程是否存活)
        /**
         * 难点：
         *  1.安全点检查（stw），是否有字节码在执行。此时
         *  2.是否已经离开了同步代码块。（判断的是拥有偏向锁的线程是否离开同步代码块）
         *
         *  是谁来撤销？？
         *      thread1 拥有偏向锁，此时正在在执行同步代码块，（还没有退出）
         *      thread2 , thread3 要来抢锁，此时撤销代码是由这些线程来执行的，所以撤销是由非线程1来执行
         */
        MarkWork markWork = myLock.getMarkWork();
        boolean isAlive = false;
        long threadID = markWork.getThreadID();
        int activeCount = Thread.currentThread().getThreadGroup().activeCount();
        Thread[] threads = new Thread[activeCount];
        int enumerate = Thread.enumerate(threads);
        for (Thread t : threads) {
            if (t.getId() == threadID) {
                isAlive = true;
                break;
            }
        }
        if (isAlive){//存活，撤销偏向锁
            markWork.setBiasedLock("0");
            markWork.setThreadID(-1L);
            markWork.setFlag("01");
        }

        //线程存活，那么撤销，此时设置成无锁状态
        if (!isAlive){//不存活
            return true;
        }



        return false;
    }
}
