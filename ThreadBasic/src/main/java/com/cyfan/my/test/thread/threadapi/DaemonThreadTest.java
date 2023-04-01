package com.cyfan.my.test.thread.threadapi;

import java.util.concurrent.TimeUnit;

public class DaemonThreadTest {

    public static void main(String[] args) {

        //守护线程睡5秒后退出
        Thread daemonThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("this is daemon thread!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //用户线程睡3秒后退出
        Thread userThread = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("this is user thread!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        daemonThread.setDaemon(true);//设置为守护线程
        userThread.setDaemon(false); //设置为用户线程

        daemonThread.start();
        userThread.start();

        //可以看到，main线程和userThread线程执行完毕之后，daemonThread线程结束了运行，只运行了3次
        //修改daemonThread为用户线程时，会运行5次打印

        //运行到这里，main线程已经退出，可以debugger查看运行的线程
        System.out.println("main thread is daemon ??"+ Thread.currentThread().isDaemon());
        System.out.println("main thread is end.......");
    }
}
