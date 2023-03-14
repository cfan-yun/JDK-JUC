package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MySynchronized {


    static volatile MyLock myLock = new MyLock();
    private boolean useBiasedLocking = true;
    private static  BiasedLocking biasedLocking = new BiasedLocking();

    static ThreadLocal<LockRecord> threadLocal = new ThreadLocal() {//当前线程中，存一份markWork和owner
        @Override
        protected LockRecord initialValue() {
            MarkWork markWork = myLock.getMarkWork();
            MarkWork owner = null;

            LockRecord lockRecord = new LockRecord();

            MarkWork markWorkClone = null;
            try {
                markWorkClone = (MarkWork) markWork.clone();

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            lockRecord.setOwner(owner);
            lockRecord.setMarkWork(markWorkClone);
            return lockRecord;
        }
    };

    private void monitorEnter() {

        /**
         * 锁升级后续实现
         * 无锁-》偏向锁-》轻量级锁-》重量级锁
         */
        if (useBiasedLocking) {//是否开启偏向锁
            fastEnter();
        } else {//走轻量级锁-》膨胀-》重量级锁
            slowEnter();
        }

    }

    /**
     * 偏锁锁加锁流程
     */
    private void fastEnter() {
        if (useBiasedLocking){//是否开启偏向锁
            boolean isOK = biasedLocking.revokeAndRebias(myLock);
            if (isOK){
                return;
            }

        }else{
            //TODO 撤销偏向锁
        }
        //走轻量级锁模式
        slowEnter();
    }

    /**
     * 轻量级锁加锁
     */
    private void slowEnter() {
        //这里很多逻辑
        /**
         * 如果是偏向锁或者无锁状态
         * lockFlag="01"
         */
        MarkWork markWork = myLock.getMarkWork();
        String lockFlag = markWork.getFlag();
        //处于偏向锁或者无锁状态
        if (lockFlag != null && "01".equals(lockFlag)) {
            markWork.setThreadID(-1L);
            markWork.setBiasedLock(null);
            //cas变更LockRecord指针
            Unsafe unsafe = MyUnsafe.getUnsafe();
            Field ptrLockRecord = null;
            try {
                ptrLockRecord = markWork.getClass().getDeclaredField("ptrLockRecord");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            long offset = unsafe.objectFieldOffset(ptrLockRecord);
            Object currentLockRecord = unsafe.getObjectVolatile(markWork, offset);
            LockRecord lockRecord = threadLocal.get();
            // 设置lockRecord -> markWork.ptrLockRecord
            //cas修改markWord中的ptrLockRecord指针指向线程栈帧中的lockRecord
            boolean isOK = unsafe.compareAndSwapObject(markWork, offset, currentLockRecord, lockRecord);
            if (isOK) {
                markWork.setFlag("00"); //设值为轻量级锁标志
                lockRecord.setOwner(markWork); //owner 指向当前线程的markWord
                return;
            }
            //修改失败开始膨胀
            //inflateEnter();
        } else if (lockFlag != null && "00".equals(lockFlag)) {//此时已经是轻量级锁
            markWork.setThreadID(-1L);
            markWork.setBiasedLock(null);
            //先获取当前线程中的lockRecord
            LockRecord lockRecord = threadLocal.get();
            //再获取markWord中的lockRecord指针
            LockRecord ptrLockRecord = markWork.getPtrLockRecord();
            //判断当前线程是的已经拥有锁,拥有锁，代表是锁重入，万事大吉
            if (ptrLockRecord != null && lockRecord != null && lockRecord == ptrLockRecord) {
                return;
            }
            //不是重入锁，开始膨胀
            //inflateEnter();
        }
        //开始锁膨胀，膨胀为重量级锁
        inflateEnter();
    }

    private void inflateEnter() {
        //锁膨胀，膨胀的结果是生成ObjectMonitor对象
        ObjectMonitor objectMonitor = inflate();
        //进入锁
        objectMonitor.enter(myLock);
    }

    /**
     * 膨胀过程只是轻量级锁膨胀为重量级锁
     *
     * @return
     */
    private ObjectMonitor inflate() {
        for (; ; ) {
            MarkWork markWork = myLock.getMarkWork();
            ObjectMonitor preMonitor = markWork.getPtrMonitor();
            //1.如果已经膨胀完毕，即已经生成了内置锁ObjectMonitor对象
            if (preMonitor != null) {
                return preMonitor;
            }

            //2.正在膨胀中,自旋等待
            String inflateStatus = markWork.getInflateStatus();
            if (inflateStatus != null && "INFLATING".equals(inflateStatus)) {
                continue;
            }

            //3.当前是轻量级锁，cas修改状态为INFLATING
            LockRecord ptrLockRecord = markWork.getPtrLockRecord();
            String lockFlag = markWork.getFlag();
            if (lockFlag != null && "00".equals(lockFlag) && ptrLockRecord != null) {//判断当前是轻量级锁
                //cas自旋更改markWord状态
                Unsafe unsafe = MyUnsafe.getUnsafe();
                Field inflateStatusFiled = null;
                try {
                    inflateStatusFiled = markWork.getClass().getDeclaredField("inflateStatus");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                long offset = unsafe.objectFieldOffset(inflateStatusFiled);
                Object currentInflateStatus = unsafe.getObjectVolatile(markWork, offset);
                //修改markWork.inflateStatus 为INFLATING
                boolean isOK = unsafe.compareAndSwapObject(markWork, offset, currentInflateStatus, "INFLATING");
                if (isOK) {//修改成功，生成重量级锁对象，并设置值到对象头markWord
                    ObjectMonitor objectMonitor = new ObjectMonitor();
                    markWork.setPtrMonitor(objectMonitor); //设置重量级锁
                    markWork.setFlag("10"); //设置为重量级锁标识
                    markWork.setPtrLockRecord(null); //设置轻量级锁为空
                    return objectMonitor;

                } else {//修改失败，自旋
                    continue;
                }


            }

        }

    }


    private void monitorExists() {

    }
}
