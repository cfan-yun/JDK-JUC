package com.cyfan.study.a01.atomic.b02.basic;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyAtomicBooleanTask implements Runnable {

    private boolean flag = false; //使用这个一定有线程安全问题,无论加不加volatile都有线程安全问题
    private AtomicBoolean automicFlag = new AtomicBoolean(false);


    @Override
    public void run() {
        try {
//            flag = true;
//            if (flag) {
            if (automicFlag.compareAndSet(false, true)){
                System.out.println(Thread.currentThread().getName() + ", 1...");
//                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ", 2...");
//                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ", 3...");
//                Thread.sleep(1000);
//                flag =  false;
                automicFlag.set(false);
            }else{
                System.out.println(Thread.currentThread().getName() + ", 没能插入到其他线程的操作中！！");
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
