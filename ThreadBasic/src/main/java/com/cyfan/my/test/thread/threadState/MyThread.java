package com.cyfan.my.test.thread.threadState;

public class MyThread extends  Thread {

    /**
     * NEW  //
     * RUNNABLE
     * BLOCKED
     * WAITING
     * TIMED_WAITING
     * TERMINATED
     */
    public MyThread() {
        System.out.println("in MyThread state is：" + this.getState());
    }

    @Override
    public void run() {
        System.out.println("in run  state1 is:" + this.getState());
        try {
            Thread.sleep(2000);
            System.out.println("in run  state2 is:" + this.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
