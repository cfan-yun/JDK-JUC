package com.cyfan.my.test.thread;

public class MyThreadTest {
    private static int THREAD_COUNT = 100000;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT;  i++){
            threads[i] = new Thread(()-> {
                System.out.println(Thread.currentThread() +  "" +calc());
            });
        }
        for (int i = 0; i < THREAD_COUNT;  i++){
            threads[i].start();
        }
        try {
            for (int i = 0; i < THREAD_COUNT;  i++){
                threads[i].join(); //等待线程执行完之后统一返回
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private static int calc() {
        int sum = 0;
        for (int i = 0; i < 10000; i++){
            for (int j = 0; j < 200; j++){
                sum+=i;
            }
        }
        return  sum;
    }
}
