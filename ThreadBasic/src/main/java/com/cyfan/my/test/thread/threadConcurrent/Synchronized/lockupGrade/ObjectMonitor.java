package com.cyfan.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * jvm内置锁对象，java 手写实现
 * 对应jvm中ObjectMonitor.cpp .hpp 中学习对应的重要属性
 * 锁入口monitorEnter 对应为jvm 源码中的InterpreterRuntime.cpp 中的monitorEnter方法
 */
public class ObjectMonitor {
    private int recursions = 0; // 记录锁重入的次数
    private Object object = null; // 锁对象
    //这里为什么要定义成volatile类型
    private volatile Thread owner = null; // 当前持有锁的线程
    private LinkedBlockingQueue cxq = null; // 多线程竞争锁时进入的队列，C源码里面本质上是个栈
    private LinkedBlockingQueue waiteSet = null;// wait方法调用后，线程进入的队列
    private LinkedBlockingQueue entryList = null;

    //重量级锁入口
    public void enter(MyLock myLock){
        //1.cas 修改owner为当前线程
        Thread currentThread = cmpAndChange(myLock);
        if (currentThread == null) {
            return;
        }
        //2.如果之前的owner指向当前线程，那么就表示重入，recursions++
        if (currentThread == Thread.currentThread()) {
            recursions++;
            return;
        }
        //3.从轻量级锁膨胀来的。
        LockRecord lockRecord = MySynchronized.threadLocal.get();
        if (lockRecord.getMarkWork() != null){
            recursions = 1;
            this.owner = Thread.currentThread();
            return;
        }

        //4.预备入队挂起
        enterI(myLock);
    }

    /**
     * 重量级锁的退出
     */
    public void exit(MyLock myLock) {
        Thread thread = Thread.currentThread();
        if (thread != this.owner){//轻量级锁膨胀来的
            //获取当前线程栈帧中的轻量级锁对象
            LockRecord lockRecord = MySynchronized.threadLocal.get();
            //获取轻量级锁对象中拷贝的对象头信息
            MarkWork head = lockRecord.getMarkWork();
            if (head != null){//不为空，说明是轻量级锁升级之后释放，将owner设置为当前对象
                owner = thread;
                recursions = 0;
            }else{
                throw new RuntimeException("不是锁的拥有者无权释放该锁！");
            }
        }

        //如果是重入锁退出， recursions--;
        if (recursions != 0){
            recursions--;

        }

        //开始选择唤醒模式
        MyUnsafe.getUnsafe();//触发屏障 ，对应c++中的fence屏障触发
        //从队列里面获取一个线程准备唤醒
        ObjectWaiter objectWaiter = (ObjectWaiter) cxq.poll();
        if (objectWaiter != null){//不为空时，代表获取cxq队列中对象成功，才能继续往下执行
            editEpilog(myLock, objectWaiter);
        }


    }

    /**
     * 唤醒线程
     * 唤醒后owner 置为空
     */
    private void editEpilog(MyLock myLock, ObjectWaiter objectWaiter) {
        //丢弃锁，将onwer置为null
        MarkWork markWork = myLock.getMarkWork();
        markWork.getPtrMonitor().setOwner(null);
        //获取线程唤醒
        Thread thread = objectWaiter.getThread();
        MyUnsafe.getUnsafe().unpark(thread);// 唤醒线程
        markWork.setPtrMonitor(null);
        objectWaiter = null;
    }


    /**
     * cas 操作修改owner对象指针
     *
     * @param myLock
     * @return
     * @throws NoSuchFieldException
     */
    private Thread cmpAndChange(MyLock myLock) {
        ObjectMonitor objectMonitor = myLock.getMarkWork().getPtrMonitor();
        Field owner = null;
        try {
            owner = objectMonitor.getClass().getDeclaredField("owner");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Unsafe unsafe = MyUnsafe.getUnsafe();
        Thread currentThread = Thread.currentThread();
        long offset = unsafe.objectFieldOffset(owner);
        boolean isOk = unsafe.compareAndSwapObject(owner, offset, null, currentThread);
        if (isOk) {//修改成功,返回null
            return null;
        }
        Thread currentOwner = objectMonitor.getOwner();
        return currentOwner;//修改失败返回当前线程
    }

    /**
     * 真正的挂起操作
     *
     * @param myLock
     */
    private void enterI(MyLock myLock) {
        //1.自旋抢锁
        if (tryLock(myLock)>0) {
            return;
        }

        //2.延迟初始化操作（模拟）
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.再次自旋抢锁，自旋10次
        for (int i = 0; i < 10; i++) {
            if (tryLock(myLock)>0){
                return;
            }
        }


        //4.到此，自旋全部失败。必须入队挂起
        ObjectWaiter objectWaiter = new ObjectWaiter();
        objectWaiter.setThread(Thread.currentThread());
        for (;;){
            try {
                cxq.put(objectWaiter);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (tryLock(myLock)>0) {
                    return;
                }
            }
        }

        //真正阻塞
        for (;;){
            if (tryLock(myLock)>0) {
                break;
            }

            Unsafe unsafe = MyUnsafe.getUnsafe();
            unsafe.park(false,0L); //挂起后线程卡在这里

            //被唤醒后再次抢锁
            if (tryLock(myLock)>0) {
                break;
            }

        }

    }

    private int tryLock(MyLock myLock) {
        for (; ; ) {
            //如果有线程还拥有重量级锁，那么直接退出
            if (owner != null){
                return 0;
            }

            Thread thread = cmpAndChange(myLock);
            //cas更新成功
            if (thread == null){
                return 1;
            }else{//cas更新失败
                return -1;
            }

        }
    }


    public int getRecursions() {
        return recursions;
    }

    public void setRecursions(int recursions) {
        this.recursions = recursions;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Thread getOwner() {
        return owner;
    }

    public void setOwner(Thread owner) {
        this.owner = owner;
    }

    public LinkedBlockingQueue getCxq() {
        return cxq;
    }

    public void setCxq(LinkedBlockingQueue cxq) {
        this.cxq = cxq;
    }

    public LinkedBlockingQueue getWaiteSet() {
        return waiteSet;
    }

    public void setWaiteSet(LinkedBlockingQueue waiteSet) {
        this.waiteSet = waiteSet;
    }

    public LinkedBlockingQueue getEntryList() {
        return entryList;
    }

    public void setEntryList(LinkedBlockingQueue entryList) {
        this.entryList = entryList;
    }

}
