package com.cyfan.my.test.thread.threadapi;

public class ThreadAPI {
    public static void main(String[] args) {
        Thread t1 = new Thread();
        t1.start();

        Thread thread1 = new Thread("thread1");
        System.out.println(thread1.getName());


        Runnable runnable =  ()->{
            System.out.println(Thread.currentThread().getName()+" run 执行了！");
        };

        Thread thread2 = new Thread(runnable, "thread2");
        System.out.println(thread2.getName());

        thread2.start();

        Thread thread3 = new Thread(runnable);
        System.out.println(thread3.getName());
        thread3.start();
    }
}
