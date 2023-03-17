package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized;

/**
 * synchronized 保证有序性、原子性、可见性
 *
 * 局部变量没有安全性问题
 * 成员变量有安全性问题
 *
 */
public class SynchronizedTest1 {

    private  int num = 0 ;//成员变量存在安全性问题

    public int getNum() {
        return num;
    }

    public static void main(String[] args) {
        SynchronizedTest1 synchronizedTest = new SynchronizedTest1();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronizedTest.printNum(1);
            }


        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronizedTest.printNum(2);
            }
        });


        t1.start();
        t2.start();


        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private  void printNum(int i){
        System.out.println("currentThread = "+Thread.currentThread().getName()+", i = " + i + ", num = "+ num);
        num++;
    }

}
