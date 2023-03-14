package com.cyfan.juc.my.test.thread.datatransport.thread2;

public class FileQueue {

    public static void main(String[] args) {
//        Consumer consumer1 = new Consumer();
//        consumer1.setName("[Consumer1]");
//        consumer1.start();//线程进入Runnable状态，等待CPU调度
//
//        Consumer consumer2 = new Consumer();
//        consumer2.setName("[Consumer2]");
//        consumer2.start();//线程进入Runnable状态，等待CPU调度

        Consumer consumer = new Consumer();
        Thread thread1 = new Thread(consumer, "[Consumer1]");
        Thread thread2 = new Thread(consumer, "[Consumer2]");
        Thread thread3 = new Thread(consumer, "[Consumer3]");
        thread1.start();
        thread2.start();
        thread3.start();
        /**
         * 存在问题
         * 1.数据共享问题
         *    fileCount 和fileIndex每次new一个Consumer都有一份，需修改为static类型，让所有线程共享一份
         *    解决方法：加static
         * 2.数据安全问题。
         *    ++操作是 非原子性操作 汇编层面是3条 主内存 -> cache中国(加操作) -> 主内存
         *    解决方法：
         *        1. synchronized
         *        2. 原子类
         */
    }
}
