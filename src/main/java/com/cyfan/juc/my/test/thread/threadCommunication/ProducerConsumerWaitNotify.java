package com.cyfan.juc.my.test.thread.threadCommunication;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产者消费者模型，学习wait/notify机制 （一个生产者，一个消费者，只能生产一个产品，消费一个产品）
 * notify 唤醒的是正在wait的线程
 * wait 线程卡死，wait放弃锁，放弃cup执行权，notify之后，需要重新枪锁
 */
public class ProducerConsumerWaitNotify {

    private final static Object LOCK = new Object();
    private static boolean hasProduct =  false;
    static List<Object> list  = new ArrayList<Object>();

    public static void main(String[] args) {

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    synchronized (LOCK){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                        if (hasProduct){//有元素等待消费者消费
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            producer();//生产者生产完成
                            //修改为hasProduct =  true
                            hasProduct = true;
                            LOCK.notify();//通知消费者消费
                        }
                    }
                }
            }

            private  void producer(){
                list.add(new Object());
                System.out.println("producer 生产了 --->" + list.get(0));

            }

        }, "producer");
        producer.start();

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    synchronized (LOCK){

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        if (hasProduct){
                            consumer();
                            hasProduct = false;
                            LOCK.notify();
                        }else{
                            try {
                                LOCK.wait(); //等待生产者生产对象
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }

            private void consumer(){
                Object o = list.get(0);
                list.remove(0);
                System.out.println("consumer消费了---->" + o);
            }


        }, "consumer");
        consumer.start();

    }
}
