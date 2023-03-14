package com.cyfan.juc.my.test.thread.hook;

public class ThreadExceptionProcessTest {

    public static void main(String[] args) {
        Thread t1 =  new Thread(()->{
            for (int i = 0; i < 2; i++) {
                System.out.println(Thread.currentThread().getName()+" is running !");
            }
            int i = 1/0; //此处抛出异常 调用Thread.dispatchUncaughtException(e);由C++回调
        }, "userThread1");

        t1.setUncaughtExceptionHandler((t,e)->{
            System.out.println(e.toString());
        });
        t1.start();

    }
}
