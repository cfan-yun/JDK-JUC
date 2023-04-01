package com.cyfan.my.test.thread.threadState.waiting;

import java.util.concurrent.TimeUnit;

public class TestStateWaiting {
    public static void main(String[] args) {
        MyThreadForWaiting myThreadForWaiting = new MyThreadForWaiting();
        System.out.println(myThreadForWaiting.getName()+" in main the thread state1 is:" + myThreadForWaiting.getState());
        myThreadForWaiting.start();
        System.out.println(myThreadForWaiting.getName()+" in main the thread state2 is:" + myThreadForWaiting.getState());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(myThreadForWaiting.getName()+" in main the thread state3 is:" + myThreadForWaiting.getState());
        MyThreadForNotify myThreadForNotify = new MyThreadForNotify();
        System.out.println(myThreadForNotify.getName()+" in main the thread state1 is:" + myThreadForNotify.getState());
        myThreadForNotify.start();
        System.out.println(myThreadForNotify.getName()+" in main the thread state2 is:" + myThreadForNotify.getState());

        System.out.println(myThreadForWaiting.getName()+" in main the thread state4 is:" + myThreadForWaiting.getState());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(myThreadForWaiting.getName()+" in main the thread state5 is:" + myThreadForWaiting.getState());
        System.out.println(myThreadForNotify.getName()+" in main the thread state3 is:" + myThreadForNotify.getState());
    }
}
