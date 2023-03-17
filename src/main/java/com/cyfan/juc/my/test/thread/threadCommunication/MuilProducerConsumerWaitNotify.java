package com.cyfan.juc.my.test.thread.threadCommunication;

/**
 * 生产者消费者模型，学习wait/notify机制 （多个生产者，多个消费者，生产多个产品品，消费多个产品）
 *        例子：生产者100， 消费者100， 最多生产10个产品
 *              生产者 -> 产品队列 -> 消费者
 * notify 唤醒的是正在wait的线程
 * wait 线程卡死，wait放弃锁，放弃cup执行权，notify之后，需要重新枪锁
 */
public class MuilProducerConsumerWaitNotify {

    private final static Object LOCK = new Object();
    private static int COUNT = 0;

    public static void main(String[] args) {

        MuilProducerConsumerWaitNotify producerConsumer = new MuilProducerConsumerWaitNotify();
        Thread[] producers = new Thread[100];
        Thread[] consumers = new Thread[100];
        for (int i = 0; i < 100; i++) {
            //生产者线程
            producers[i] = new Thread(()->{
                for (;;) {//此处必须加死循环，不加死循环，那么当该线程被notifyAll()唤醒并抢到锁之后，继续 LOCK.wait();之后执行，相当于没有进行消费
                    synchronized (LOCK) {
                        if (COUNT < 10) {//不足10个产品
                            producerConsumer.producer();//生产
                            LOCK.notifyAll();// 唤醒的是所有wait的线程，包含生产者和消费者线程
                            break;
                        } else {//有10个产品，那么当前生产者线程wait
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

            },"producer-"+i);

            //消费者线程
            consumers[i] = new Thread(()->{
                for (;;){//此处必须加死循环，不加死循环，那么当该线程被notifyAll()唤醒并抢到锁之后，继续 LOCK.wait();之后执行，相当于没有进行消费
                    synchronized (LOCK){
                        if (COUNT > 0){//产品队列中存在产品，进行消费
                            producerConsumer.consumer();
                            LOCK.notifyAll(); // 唤醒的是所有的wait的线程，包含生产者和消费者线程
                            break;
                        }else {//产品队列中没有产品，阻塞等待，直到被notify唤醒消费
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            },"consumer-"+i );

        }

        for (int i = 0; i < 100; i++) {
            producers[i].start();
        }


        for (int i = 0; i < 100; i++) {
            consumers[i].start();
        }




    }


    public void producer(){
        System.out.println(Thread.currentThread().getName()+", 生产了-->" + (++COUNT));
    }

    public void consumer(){
        System.out.println(Thread.currentThread().getName()+ ", 消费了-->"+ (COUNT--));
    }
}
