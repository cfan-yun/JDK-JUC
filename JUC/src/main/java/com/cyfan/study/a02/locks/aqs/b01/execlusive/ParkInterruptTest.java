package com.cyfan.study.a02.locks.aqs.b01.execlusive;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 测试 park 被 unpark唤醒和中断唤醒的区别： 配合ParkUnParkTest看
 * *  unpark唤醒：唤醒1次。
 * *  interrupt唤醒：会让park永远失效，interrupt 的操作：1.中断标记修改为true.2,.unpark唤醒
 * *  总结：c/c++ 中 park 将 _counter 改成0 只要 _counter 为1 或者线程为中断状态，那么park就不能阻塞线程，park只会消耗counter,但是不能消耗中断（不会修改中断状态）；
 * *               unpark 将_counter 改成1  如果线程阻塞，那么将其唤醒，无论调用几次unpark，counter 的值只能为1
 */
public class ParkInterruptTest {

    public static void main(String[] args) {

        Thread mainThread = Thread.currentThread();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2); // 休眠两秒
                System.out.println(Thread.currentThread().getName() + ", 休眠2s后，唤醒main线程！");

                //LockSupport.unpark(mainThread);//子线程唤醒main线程，这里唤醒main线程一次之后，for循环中再次将main线程挂起
                mainThread.interrupt(); //main线程中断，这里直接导致后续的LockSupport.park();失效，那么为什么中断之后，park会hold不住呢？？？
                //interrupt 的操作：1.中断标记修改为true.2,.unpark唤醒  ,park 方法会去检查中断标记，为true直接返回，阻塞不住

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            System.out.println("开始阻塞main线程！");
            LockSupport.park();// 阻塞main线程
            //解决中断导致的park失效， isInterrupted 方法不行，这个方法不会清除中断标记位
            boolean interrupted = Thread.interrupted();// 清除中断标记(true -> false)，返回值是是否发生中断，是清除标记之前的标记值(true)
            if (interrupted){
                System.out.println("发生了中断的，并且interrupted 清除了中断标记");
            }
            System.out.println("main线程被唤醒！");
        }

    }
}
