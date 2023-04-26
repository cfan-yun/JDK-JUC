package com.cyfan.study.a02.locks.aqs.b01.execlusive;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


/**
 * park:C的伪代码
 * * if (_counter > 0 ){
 * *    _counter = 0;
 * *    return; // 没hold住，线程阻塞不住
 * * }
 * * if(发生中断[即中断标记为true]){
 * *     return; // 没hold住，线程阻塞不住
 * *  }
 * *  阻塞当前线程 // 线程将会从这里被唤醒
 * *  if(_counter > 0 ){
 * *      _counter = 0;
 * *  }
 * *****************************************************
 * unpark:C的伪代码
 * * if(_counter < 1){
 * *    _counter = 1
 * *    唤醒线程
 * * }
 * *****************************************************
 * interrupt:C的伪代码
 * * if(中断标记 == false){
 * *     中断标记 = true; // 1.修改中断标记
 * * }
 * * unpark();// 2.这里会唤醒
 * <p>
 * * 1.线程启动_counter的初始值是0
 * * 2.所以发生中断之后，park是hold不住的，阻塞不住。解决方案？？？？？ 清除中断标记 使用 interrupted 清除中断标记，将中断标记由true 修改为false
 * * 总结：c/c++ 中 park 将 _counter 改成0 只要 _counter 为1 或者线程为中断状态，那么park就不能阻塞线程，park只会消耗counter,但是不能消耗中断（不会修改中断状态）；
 * *               unpark 将_counter 改成1  如果线程阻塞，那么将其唤醒，无论调用几次unpark，counter 的值只能为1
 */
public class ParkUnParkTest {

    public static void main(String[] args) {
//        LockSupport.park();// 第一次park时，counter =  0 中断标记为false 此时线程会阻塞。执行park 会将 counter 修改为0
//        System.out.println("=====");

//        LockSupport.unpark(Thread.currentThread()); // 先执行 unpark ，会将 counter 修改为 1，中断标记为false
//        System.out.println("=====");
//        LockSupport.park(); //这里判断时，counter =  1 > 0 阻塞不住，将counter修改为0 之后返回,中断标记位为false
//        System.out.println("=====");
//        LockSupport.park(); // 再次阻塞时，counter =  = 0, 可以正常阻塞 中断标记位位false
//        System.out.println("=====");
        Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2); // 休眠2秒
                System.out.println("main线程中断");
                mainThread.interrupt();// main线程中断，这里会导致，main线程的park失效
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            System.out.println("main 线程挂起!");
            LockSupport.park();// 挂起10次， park检测到中断（中断标记为true）之后，直接返回，没有阻塞住，如何解决？？？
            //解决中断导致的park失效， isInterrupted 方法不行，这个方法不会清除中断标记位
            boolean interrupted = Thread.interrupted();// 清除中断标记(true -> false)，返回值是是否发生中断，是清除标记之前的标记值(true)
            if (interrupted) {
                System.out.println("发生了中断的，并且interrupted 清除了中断标记");
            }
            System.out.println("=====");
        }


    }
}
