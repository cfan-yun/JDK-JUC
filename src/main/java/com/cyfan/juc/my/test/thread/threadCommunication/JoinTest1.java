package com.cyfan.juc.my.test.thread.threadCommunication;


/**
 *
 * join 方法上是有synchronized锁的
 *  锁对象是调用join方法的t1线程对象
 * join的本质是调用了wait方法，wait阻塞的是是启动t1线程的的父线程
 *1.join 方法中，父线程调用了wait方法阻塞，一直等待，那么唤醒操作是再什么时候唤醒的？？？？？
 *  猜测在c++层面唤醒了父线程
 *  1.1.猜测线程结束时，会去唤醒其他线程
 *   验证：thread.cpp -> thread::exit -> ensure_join(this) -> ObjectLocker 构造函数加锁->  lock.notify_all(thread) ->ObjectLocker　析构函数解锁
 *   在t1 线程退出的时候，对其他线程进行了唤醒操作。
 *
 * thread1.join -》 以thread1作为锁对象-》调用了object.wait(main 卡死)-》在线程thread1退出时，以tread1作为锁对象，加锁-》 调用notifyAll唤醒 -》解锁
 *
 *
 */
public class JoinTest1 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 start....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread1 end....");
        }//t1线程结束时，会在jvm的c++层面以t1线程作为锁对象进行加锁，唤醒，释放锁。
        , "thread1");

        Thread thread2 = new Thread(() -> {

            synchronized (thread1){//线程1作为锁对象加锁
                System.out.println("thread2 start....");
                try {
                    thread1.wait();//线程1卡死，卡死了被谁唤醒了呢？？？，被thread1 线程退出时，进行了唤醒操作。

                    //Thread.sleep(5000);//休眠5秒，按正常猜测thread1肯定已经结束
                    //System.out.println("t1.isAlive = "+thread1.isAlive()); // thread1 休眠了2秒，thread1 end 已经打印但是isAlive =  true,
                    // 证明了，thread1线程结束退出时，会给tread1加锁
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread2 wakeup....");
            }

        }, "thread2");

        thread1.start();
        thread2.start();

        Object obj = new Object();


        Thread thread3= new Thread(() -> {
            System.out.println("thread3 start....");
            synchronized (obj){ // 1、加锁
                obj.notifyAll(); // 2、唤醒 -- 本质上只是挪动节点位置（waitSet -> entryList / cxq）
            }                   // 3、解锁 -- 这里才会去释放锁，唤醒线程(从 entryList / cxq 中获取节点唤醒)
        }, "thread3");
        thread3.start();


        //验证此处  synchronized (obj){} 锁退出时，没有唤醒thread1线程
        Thread thread4 = new Thread(() -> {

            synchronized (obj){
                System.out.println("thread4 start....");
                try {
                    obj.wait();//线程1卡死
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread4 wakeup....");
            }

        }, "thread4");
        thread4.start();

    }

}
