package com.cyfan.my.test.threadPool.queue;

import com.cyfan.my.test.thread.uilt.Sleep;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue(同步移交)
 *               此队列里面没有容器，一个生产者线程，当他生产产品（put）的时候，如果当前没有消费者想要消费
 *               那么此生产者线程必须阻塞，等待一个消费者线程来调用(take),take操作会唤醒生者线程，同时消费者
 *               线程进行消费，所以这叫做一次配对
 *
 *               //生产者不启动，消费者线程卡住
 *               //消费者不启动，生产者线程也会卡住
 *               1.put 和take方法必须配对
 *               2.默认非公平，后启动的生产者先被消费
 *                  公平使用队列实现，非公平使用栈实现
 */
public class SynchronousQueueTest {

    public static void main(String[] args) {

        //公平与非公平(默认非公平)
        SynchronousQueue<Object> queue = new SynchronousQueue<>();
        Thread producer = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+" start ...");
                queue.put(1);
                System.out.println(Thread.currentThread().getName()+" end ...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "producer");

        Thread producer1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+" start ...");
                queue.put(2);
                System.out.println(Thread.currentThread().getName()+" end ...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "producer1");

        Thread consumer = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+" start ...");
                Object take = queue.take();
                System.out.println("---->"+take);
                System.out.println(Thread.currentThread().getName()+" end ...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "consumer");

        producer.start(); //生产者不启动，消费者线程卡住
        Sleep.mySleep(2000);
        System.out.println(Thread.currentThread().getName()+"，休眠2秒");
        producer1.start(); // producer1 后启动，producer1先被消费
        consumer.start();//消费者不启动，生产者线程也会卡住

    }
}
