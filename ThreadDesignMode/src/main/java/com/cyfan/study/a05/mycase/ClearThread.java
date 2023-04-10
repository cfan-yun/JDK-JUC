package com.cyfan.study.a05.mycase;

public class ClearThread<T> extends Thread {

    private final MyQueue<T> queue;

    public ClearThread(MyQueue<T> queue, String name) {
        super(name);
        this.queue = queue;
    }


    @Override
    public void run() {
        try {
            while (true) {

                Thread.sleep(1000);
                System.out.println("################"+getName()+"clear ############");
                queue.clear();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
