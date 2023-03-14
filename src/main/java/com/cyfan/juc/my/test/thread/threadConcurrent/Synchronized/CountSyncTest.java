package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized;

/**
 * 不加synchronized关键字的情况下，index 可能超越500， 最大等于500+启动的线程数-1
 * 是由于不同线程交替执行指令，操作相同变量引起的
 *
 */
public class CountSyncTest implements  Runnable{

    private int index = 0;
    private static int COUNT = 500;

    private Object lock = new Object();

    public static void main(String[] args) {
        CountSyncTest countSyncTest = new CountSyncTest();
        Thread t1 = new Thread(countSyncTest);
        Thread t2 = new Thread(countSyncTest);
        Thread t3 = new Thread(countSyncTest);

        t1.start();
        t2.start();
        t3.start();
    }

    @Override
    public void run() {
        while (true){
            //增加锁之后，正常有序执行
            synchronized (lock){// monitor enter
                if(index >  COUNT){
                    break;
                }
                mySleep(10); //休眠
                System.out.println("currentThread = "+Thread.currentThread().getName() + ", index = " + index);
                index++;
            }//monitor exit

        }


    }


    static void mySleep(long time){
        long start = System.currentTimeMillis();
        long end;
        do {
            end =  System.currentTimeMillis();
        }while (start+time>=end);
    }
}
