package com.cyfan.my.test.thread.threadConcurrent.Volatile;

import java.io.File;

/**
 * 共享变量进行线程间通信测试
 * 代码要点：while中
 *  1.不要加多余的代码，可能影响测试效果
 *  2.不要使用sleep方法，也会影响测试效果
 *  3.生产者线程一定在消费者线程后启动
 *  测试结果：
 *      四种情况，都能使消费者线程获得最新的共享变量值。
 *  总结：究其原因，其实是，synchronized关键字，在其代码块或方法执行完时，会将线程副本中的变量1，强制去和主存进行一个同步操作。
 *  synchronized 保证可见性、有序性(防止指令重排了，自然有序)、原子性，官方说法。
 *  volatile 保证可见性、防止指令重排。
 *
 */
public class VariableShareTest {

    static  int i = 0; //共享变量
    static Object object = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + i);
            while (true){
                //1.打印：这段代码中有synchronized
                //System.out.println(">>>");
                //2.有synchronized
                //synchronized (object){}
                //3.sleep方法在c++底层有锁
                /*try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //4.这段代码中也有synchronized
                //new File("");
                //线程内部存储的变量副本
                if (i ==  1){
                    System.out.println(Thread.currentThread().getName()+", i = "+i);
                    break;
                }
                //synchronized (object){}
                //System.out.println(">>>");
                /*try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }, "consumer1").start();

        Thread producer = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + i);
            //增加休眠方法后，consumer线程获取不到i 最新值
            mySleep(1000000000);
            i = 1;
        }, "producer");
        producer.start();
        //producer.join();//这里join之后consumer2能读到i =  1,但是consumer1读不到
        //究其原因是因为每个线程里面留存的都是i变量的副本，consumer2是在producer线程执行完成修改i变量完成之后才启动的，可以读取到最新值
        //没加volatile 变量值改变，consumer1先启动加载的初始值0，虽然producer 进行了修改，但是consumer1感知不到。
        //变量的副本在什么时候写回内存，这个不清楚

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + i);
            while (true){
                if (i ==  1){
                    System.out.println(Thread.currentThread().getName()+", i = "+i);
                    break;
                }

            }
        }, "consumer2").start();

    }




    static void mySleep(long time){
        long start = System.nanoTime();
        long end;
        do {
            end =  System.nanoTime();;
        }while (start+time>=end);
    }
}
