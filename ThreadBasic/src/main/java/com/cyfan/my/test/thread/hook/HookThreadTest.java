package com.cyfan.my.test.thread.hook;

import java.util.concurrent.TimeUnit;

/**
 * 测试
 *  1.钩子线程在什么时候执行？
 *  2.钩子线程与DestroyJavaVM线程的关系。
 */
public class HookThreadTest {

    public static void main(String[] args) {

        //用户线程,并在用户线程中获取到DestroyVM线程信息
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println("this is a user thread running !");
                    TimeUnit.SECONDS.sleep(1); //休眠1 秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //获取到DestroyVM线程
            Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
            int enumerate = Thread.enumerate(threads);
            for (int i = 0; i < enumerate; i++) {
                //System.out.println(threads[i].getName());
                if("DestroyJavaVM".equals(threads[i].getName())){//这个线程可以理解为是main线程的延续，在main线程中是获取不到的，只能在其他活动的用户线程中获取到
                    System.out.println("DestroyJavaVM is running !");
                }
            }
        },"userThread").start(); //启动用户线程

        //添加一个钩子线程,这个钩子线程是在所有的用户线程执行完毕之后，在DestroyJavaVM线程阻塞被唤醒之后执行的thread.cpp中的Threads::destroy_vm()方法中thread->invoke_shutdown_hooks();
        //通过c++中invoke_shutdown_hooks(); 回调java中的 Shutdown 类中执行 shutdown()， 这个方法是在c++中回调的
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " is running! \n");

            //钩子线程执行时仍然活动的线程
            Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
            int enumerate = Thread.enumerate(threads);
            for (int i = 0; i < enumerate; i++) {
                System.out.printf("["+threads[i].getName()+"],");
            }
        }, "hookThread"));


        System.out.println("main thread is end...");

    }
}
