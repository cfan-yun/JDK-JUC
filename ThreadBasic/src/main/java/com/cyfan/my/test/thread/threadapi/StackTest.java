package com.cyfan.my.test.thread.threadapi;

public class StackTest {

    private static int COUNT = 0;

    public static void main(String[] args) {

        //new Thread(()->{pressStackTest();}, "thread1").start();
        //设置stackSize 为2兆 Thread(ThreadGroup group, Runnable target, String name, long stackSize)
        new Thread(Thread.currentThread().getThreadGroup(), ()->{pressStackTest();},"myThread", 1024*1024*2).start();
//        pressStackTest();

    }

    /**
     * 压栈测试
     */
    public static  void pressStackTest(){
        StackTest stackTest = new StackTest();
        try {
            //main 线程中 stackCount = 27173
            stackTest.count();
        } catch (Error e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName()+"Thead stackCount = "+COUNT);
        }
    }

    public void count(){
        COUNT++;
        count();
    }
}
