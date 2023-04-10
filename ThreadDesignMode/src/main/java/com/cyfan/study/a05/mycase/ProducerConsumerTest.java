package com.cyfan.study.a05.mycase;


public class ProducerConsumerTest {

    public static void main(String[] args) {
        MyQueue<Product> queue = new MyQueue<Product>(10);

        for (int i = 0; i < 10; i++) {
            new ProducerThread<Product>(queue, "producer"+i).start();
            new ConsumerThread<Product>(queue,"consumer"+i).start();
            new ClearThread<Product>(queue, "clear"+i).start();
        }

    }
}
