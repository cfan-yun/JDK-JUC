package com.cyfan.study.a02.locks.aqs.reentraintlock;

/**
 * 1.为什么要在finally中调用 lock.unlock()??
 * *    保证一定会解锁
 * 2.为什么lock.lock() 不能放在ry里面
 * *    放在try中会导致lock.lock()报错时，异常被吞噬掉
 * *    加不加catch throw new RuntimeException,都会被吞噬掉
 */
public class CorrectUseLockTest {

    static MyReentrantLock lock = new MyReentrantLock();


    public static void main(String[] args) {
        new Thread(() -> {
            //lock.lock();//加锁
            try {
                lock.lock();//加锁 // 算数异常被吞噬了
                System.out.println("执行业务逻辑");
            } catch (Exception e) {//catch里面也没有的
                // lock.unlock();//解锁
                //e.printStackTrace();
                throw new RuntimeException(e);//抛出来也没用，还是被吞噬了
            } finally {
                lock.unlock();//解锁
            }


        }).start();
    }
}
