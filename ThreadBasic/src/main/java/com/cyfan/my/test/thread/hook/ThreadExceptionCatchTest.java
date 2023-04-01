package com.cyfan.my.test.thread.hook;

public class ThreadExceptionCatchTest {

    public static void main(String[] args) {

        try {
            new Thread(()->{
                for (int i = 0; i < 2; i++) {
                    System.out.println(Thread.currentThread().getName()+" is running !");
                }
                int i = 1/0; //此处抛出异常
            }, "userThread").start();

            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("===========");
            e.printStackTrace(); //此处不能获取到异常。
        }
    }
}
