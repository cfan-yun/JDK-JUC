package com.cyfan.my.test.thread.threadState.blocked;

import java.util.concurrent.TimeUnit;

public class TestStateBlocked {

    public static void main(String[] args) {
        MyThreadFroBlocked myThreadFroBlocked = new MyThreadFroBlocked();
        System.out.println(myThreadFroBlocked.getName()+ " in main state1 is:" + myThreadFroBlocked.getState());
        myThreadFroBlocked.start();
        System.out.println(myThreadFroBlocked.getName()+ " in main state2 is:" + myThreadFroBlocked.getState());

        MyThreadFroBlocked2 myThreadFroBlocked2 = new MyThreadFroBlocked2();
        System.out.println(myThreadFroBlocked2.getName()+ " in main state1 is:" + myThreadFroBlocked2.getState());
        myThreadFroBlocked2.start();
        System.out.println(myThreadFroBlocked2.getName()+ " in main state2 is:" + myThreadFroBlocked2.getState());

        System.out.println(myThreadFroBlocked.getName()+ " in main state3 is:" + myThreadFroBlocked.getState());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(myThreadFroBlocked.getName()+ " in main state4 is:" + myThreadFroBlocked.getState());
        System.out.println(myThreadFroBlocked2.getName()+ " in main state3 is:" + myThreadFroBlocked2.getState());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(myThreadFroBlocked.getName()+ " in main state4 is:" + myThreadFroBlocked.getState());
        System.out.println(myThreadFroBlocked2.getName()+ " in main state3 is:" + myThreadFroBlocked2.getState());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(myThreadFroBlocked.getName()+ " in main state5 is:" + myThreadFroBlocked.getState());
        System.out.println(myThreadFroBlocked2.getName()+ " in main state4 is:" + myThreadFroBlocked2.getState());

    }

}
