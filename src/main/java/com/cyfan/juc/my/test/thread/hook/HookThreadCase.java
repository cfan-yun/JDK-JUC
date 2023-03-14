package com.cyfan.juc.my.test.thread.hook;

/**
 * 比如有一个任务，一直在运行，发生异常（main线程，main结束）
 *
 * - 此时需要上报异常，邮件通知相关人员
 * - 此时需要释放资源，网络，数据库连接等资源
 *
 * 解决方案：在程序开始执行前就，加两个钩子。
 */
public class HookThreadCase {

    public static void main(String[] args) {
        //上报异常，邮件通知相关人员
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println(">>>>>>>>>>>> notify somebody!");
        }));

        //要释放资源，网络，数据库连接等资源
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println(">>>>>>>>>>>> release some resources!");
        }));

        new Thread(()->{
            for (int i = 0; i < 2; i++) {
                System.out.println(Thread.currentThread().getName()+" is running !");
            }
            int i = 1/0; //此处抛出异常
        }, "userThread").start();

    }
}
