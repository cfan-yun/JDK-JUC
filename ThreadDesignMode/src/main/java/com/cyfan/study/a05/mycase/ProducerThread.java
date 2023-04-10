package com.cyfan.study.a05.mycase;

/**
 * 生产者线程
 */
public class ProducerThread<T> extends Thread {
    private final MyQueue<T> queue;
    private static long ID;

    public ProducerThread(MyQueue<T> queue,String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Product product = new Product(nextId());
                queue.put((T) product);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static long nextId() {
        return ID++;
    }
}
