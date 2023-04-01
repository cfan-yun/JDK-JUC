package com.cyfan.my.test.thread.threadState;

import java.util.concurrent.TimeUnit;

public class TestState {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        System.out.println("in main state1 is:" + myThread.getState());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myThread.start();
        System.out.println("in main state2 is:" + myThread.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in main state3 is:" + myThread.getState());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in main state4 is:" + myThread.getState());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in main state5 is:" + myThread.getState());
    }
}
