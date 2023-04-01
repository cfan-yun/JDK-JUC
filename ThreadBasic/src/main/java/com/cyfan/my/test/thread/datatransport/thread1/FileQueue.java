package com.cyfan.my.test.thread.datatransport.thread1;

public class FileQueue {

    public static void main(String[] args) {
        Consumer consumer1 = new Consumer();
        consumer1.setName("[Consumer1]");
        consumer1.start();//线程进入Runnable状态，等待CPU调度

        Consumer consumer2 = new Consumer();
        consumer2.setName("[Consumer2]");
        consumer2.start();//线程进入Runnable状态，等待CPU调度

        Consumer consumer3 = new Consumer();
        consumer3.setName("[Consumer3]");
        consumer3.start();//线程进入Runnable状态，等待CPU调度



        /**
         * 存在问题
         * 1.数据共享问题
         *    fileCount 和fileIndex每次new一个Consumer都有一份，需修改为static类型，让所有线程共享一份
         * 2.数据安全问题。
         *    fileIndex++非原子性操作
         *
         */
    }
}
