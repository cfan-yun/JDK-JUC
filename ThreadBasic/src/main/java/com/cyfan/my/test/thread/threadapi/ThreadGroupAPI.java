package com.cyfan.my.test.thread.threadapi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThreadGroupAPI {

    public static void main(String[] args) throws InterruptedException {

        /**
         * 线程组构造函数
         *      线程组、线程的级联关系   树状
         */
        //获取main线程组
        ThreadGroup main = Thread.currentThread().getThreadGroup();
        System.out.println("main threadGroup = " + main);

        //获取main线程组的父线程组为system
        ThreadGroup system = main.getParent();
        System.out.println("mainParent threadGroup = " + system);

        //获取main线程组的父线程的父线程
        System.out.println("systemParent threadGroup = " + system.getParent());

        //创建自定义线程组
        ThreadGroup myGroup = new ThreadGroup("myGroup");
        System.out.println("myGroup = " + myGroup);
        System.out.println("myGroupParent = " + myGroup.getParent());

        //实现R Runnable 接口的对象实例
        Runnable runnable = () -> {
            try {
                sleepAndPrintCurrentThreadName(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //创建两个线程
        Thread thread1 = new Thread(myGroup, runnable, "thread1");
        Thread thread2 = new Thread(myGroup, runnable, "thread2");

        //获取myGroup线程组下活动的线程和活动的线程组
        int activeCount = myGroup.activeCount();
        int activeGroupCount = myGroup.activeGroupCount();

        System.out.println("myGroup activeCount = " + activeCount);
        System.out.println("myGroup activeGroupCount = " + activeGroupCount);

        //启动线程
        thread1.start();
        thread2.start();


        activeCount = myGroup.activeCount();
        activeGroupCount = myGroup.activeGroupCount();

        System.out.println("myGroup activeCount = " + activeCount);
        System.out.println("myGroup activeGroupCount = " + activeGroupCount);

        //新建自定义线程组myGroup1，父线程组为myGroup
        ThreadGroup myGroup1 = new ThreadGroup(myGroup, "myGroup1");
        System.out.println("myGroup1 = " + myGroup1.getParent());
        System.out.println("myGroup1Parent = " + myGroup1.getParent());

        activeCount = myGroup.activeCount(); // 活动的线程数
        activeGroupCount = myGroup.activeGroupCount(); //活动的线程组数

        System.out.println("myGroup activeCount = " + activeCount);
        System.out.println("myGroup activeGroupCount = " + activeGroupCount);


        /**
         * 线程组迭代
         */
        //新建自定义线程组 myGroup1_2，父线程组为myGroup
        ThreadGroup myGroup1_2 = new ThreadGroup(myGroup, "myGroup1_2");
        //新建线程，所属线程组为myGroup1_2
        Thread thread3 = new Thread(myGroup1_2, runnable, "thread3");
        //启动线程
        thread3.start();
        System.out.println("thread1:" + thread1.getState() + ",thread2:" + thread2.getState() + ",thread3:" + thread3.getState());

        System.out.println("mainGroup activeCount :" + main.activeCount());

        Thread thread4 = new Thread(() -> {//该线程与main线程共用线程组

            Thread thread5 = new Thread(() -> {//该线程与thread4共用线程组
                while (true){
                    try {
                        sleepAndPrintCurrentThreadName(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "thread5");
            System.out.println("thread5.getState() 1= " + thread5.getState());
            thread5.start();
            System.out.println("thread5.getState() 2= " + thread5.getState());
            try {
                sleepAndPrintCurrentThreadName(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread5.getState() 3= " + thread5.getState());
        }, "thread4");
        System.out.println("thread4.getState() 1= " + thread4.getState());
        thread4.start();
        System.out.println("thread4.getState() 2= " + thread4.getState());

        Thread.sleep(100); //等待thread5 new出来

        //获取main线程组下所有活动的线程
        Thread[] threads = new Thread[main.activeCount()];
        int enumerate = main.enumerate(threads);                 //该方法获取到的是线程main线程组下的所有活动的线程
        System.out.println("enumerate = " + enumerate);

        //打印main线程组下所有活动的线程名称
        Arrays.asList(threads).forEach((c) -> System.out.println("[thread name]" + c.getName()));

        //获取main线程组下所有活动的线程组
        ThreadGroup[] threadGroups = new ThreadGroup[main.activeGroupCount()];
        int enumerateGroup = main.enumerate(threadGroups);//该方法获取到的是线程main线程组下的所有活动的线程
        main.enumerate(threadGroups,false);//不递归获取，只获取直接下层，默认为true
        System.out.println("enumerateGroup = " + enumerateGroup);

        //打印main线程组下所有线程组的名称
        Arrays.asList(threadGroups).forEach((c) -> System.out.println("[threadGroup name]" + c.getName()));


        /**
         * 批量中断线程
         */

        ThreadGroup myGroup2 = new ThreadGroup("myGroup2");
        for (int i = 0; i < 10;  i++){
            new Thread(myGroup2, ()-> {
                try {
                    while(true){sleepAndPrintCurrentThreadName(1);}
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " is interrupt !");
                    e.printStackTrace();
                }
            }, "thread_"+(i+1)).start();
        }

        Thread[] threads1 = new Thread[myGroup2.activeCount()];
        myGroup2.enumerate(threads1);
        Arrays.asList(threads1).forEach((c)-> System.out.println(c.getName()+":"+c.getState()));
        myGroup2.interrupt();//批量中断线程
        System.out.println("======================批量中断完成");
        Arrays.asList(threads1).forEach((c)-> System.out.println(c.getName()+":"+c.getState()));
        Thread.sleep(10000);
        Arrays.asList(threads1).forEach((c)-> System.out.println(c.getName()+":"+c.getState()));
    }


    public static void sleepAndPrintCurrentThreadName(long seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds); //休眠1 秒
        System.out.println(Thread.currentThread().getName() + " run method executed!");
    }
}
