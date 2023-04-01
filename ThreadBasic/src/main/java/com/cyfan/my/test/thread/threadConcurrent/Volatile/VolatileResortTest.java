package com.cyfan.my.test.thread.threadConcurrent.Volatile;

import com.cyfan.my.test.thread.uilt.Sleep;

public class VolatileResortTest {
    volatile int i;
    int j;
    public static void main(String[] args) {
        VolatileResortTest test = new VolatileResortTest();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                test.test();
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                test.test();
            }
        }).start();
    }


    public void test(){
        Sleep.mySleep(10);
        i++;
        if ((j = i )> 2) {
            System.out.println(Thread.currentThread().getName() + ",result ="+j);
        }
    }
}


