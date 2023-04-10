package com.cyfan.study.a05.mycase;

/**
 * 消费者线程
 * @param <T>
 */
public class ConsumerThread<T> extends Thread {
    private final MyQueue<T> queue;

    public ConsumerThread(MyQueue<T> queue, String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                T take = queue.take();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
