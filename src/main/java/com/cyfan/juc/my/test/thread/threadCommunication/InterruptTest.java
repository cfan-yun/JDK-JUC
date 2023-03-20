package com.cyfan.juc.my.test.thread.threadCommunication;

import com.cyfan.juc.my.test.thread.uilt.Sleep;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * interrupt 机制 线程的中断机制   结论：不中断，反而是去进行唤醒，
 *  1.检验是不是字面意思的中断，发现并不是，那么其内部发生了什么？？？？？
 *    1.源码解析：Thread.interrupt -> interrupt0 -> thread.c -> JVM_Interrupt -> jvm.cpp -> JVM_Interrupt ->  Thread::interrupt -> thread.cpp
 *     ->  os::interrupt -> os_linux.cpp -> _SleepEvent.unPark(sleep 挂起的线程) / parker.unPark(unsafe.park 挂起的线程) / _ParkEvent.unPark (synchronized 挂起的线程，包含wait挂起的)
 *    2.实际就干两件事情：
 *      1.设置中断标记位(设置一个true)
 *      2.根据park的方式，进行unPark唤醒（能够响应中断interrupt的三种方式）
 *          1._SleepEvent.unPark(sleep 挂起的线程)
 *          2.parker.unPark(unsafe.park 挂起的线程)
 *          3._ParkEvent.unPark (synchronized 挂起的线程，包含wait挂起的)
 *    3.三种响应中断的方式，机制是否一样？？？？？？
 *          1.sleep响应中断：执行sleep时，死循环检查线程是否被中断，被中断，则抛出中断异常
 *          2.wait响应中断：执行wait时，线程进入waitSet队列并挂起，wait被interrupt唤醒之后继续抢锁，抢到锁之后继续往下执行,被中断，并抛出异常
 *
 * 中断标志是在调用interrupt 方法时，在jvm级别由c++设置为true的
 *  Thread.isInterrupted()
 *
 *  Thread.interrupted();                  //static (class)     执行之后返回中断标志，并清除中断标志  currentThread().isInterrupted(true);
 *  Thread.currentThread.isInterrupted();    //non-static(obj)    执行之后返回中断标志，不清除中断标志 currentThread().isInterrupted(false);
 */
public class InterruptTest {
    private static final int COUNT = 10;
    private static  final Object OBJ =  new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread main = Thread.currentThread();
        //thread1中中断main线程
        Thread thread1 = new Thread(() -> {
            Sleep.mySleep(5000);
            System.out.println(Thread.currentThread().getName()+"启动，中断main线程!!");
            main.interrupt();//执行中断
            System.out.println(Thread.currentThread().getName()+"结束");
        }, "thread1");
        thread1.start();

        //1.检验是不是字面意思的中断，发现并不是
        //2.1.唤醒在_SleepEvent上等待的线程，_SleepEvent用于实现sleep
//        for (int i = 0; i < COUNT; i++){
//            // 1.模拟sleep中断，线程sleep被interrupt唤醒后抛出中断异常
//            //System.out.println(">>>>>>>>>>>>Main park start...");
//            //Thread.sleep(1000); // _SleepEvent park sleep后会挂起，被interrupt唤醒后抛出sleep中断异常
//            //System.out.println(">>>>>>>>>>>>Main park end...");
//            // 2.模拟线程卡住，interrupt执行后线程被唤醒
//            //Sleep.mySleep(1000); // 此处不能调用Thread.sleep方法，这里sleep内部有中断判断。
//            //System.out.println(" this is main thread !!!");
//        }

        //2.2.唤醒parker上等待的线程，parker用于实现Unsafe的park和unPark方法,即interrupt() 可以唤醒  unsafe.park 挂起的线程
//        Unsafe unsafe = getUnsafe();
//        System.out.println(">>>>>>>>>>>>Main park start...");
//        unsafe.park(false, 0L);//main线程挂起,直到thread1执行   main.interrupt(); 后线程被唤醒
//        System.out.println(">>>>>>>>>>>>Main park end...");


        // 2.3.唤醒在_ParkEvent上等待的线程，_ParkEvent用于实现synchronized关键字
//        synchronized (OBJ){
//            System.out.println(">>>>>>>>>>>>Main park start...");
//            OBJ.wait(); //_ParkEvent park thread 进入waitSet队列后并挂起， 被interrupt唤醒后，抛出中断异常
//            System.out.println(">>>>>>>>>>>>Main park end...");
//        }

        Sleep.mySleep(6000);//在thread1 休眠5秒后，确保main.interrupt();执行
        boolean objOne = Thread.currentThread().isInterrupted();
        boolean objTwo = Thread.currentThread().isInterrupted();
        boolean staticOne = Thread.interrupted();
        boolean staticTwo = Thread.interrupted();
        System.out.println("objOne = "+objOne + ", objTw = " + objTwo + ", staticOne = "+staticOne + ", staticTwo = "+staticTwo);
    }


    public static Unsafe getUnsafe() {
        Field field;
        try {

            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
