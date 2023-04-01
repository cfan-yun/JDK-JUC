package com.cyfan.my.test.thread.threadState.waiting;

public class MyThreadForNotify extends Thread{

//    Object o =  new Object();
    static  Object o = MyLock.obj;
    /**
     * NEW  //
     * RUNNABLE
     * BLOCKED
     * WAITING
     * TIMED_WAITING
     * TERMINATED
     */
    public MyThreadForNotify() {
        System.out.println(this.getName() + "in MyThreadForWaiting state is：" + this.getState());
    }

    @Override
    public void run() {
        System.out.println(this.getName() + "in run  state1 is:" + this.getState());
        synchronized (o){
            System.out.println(this.getName() + "in run  synchronized state1 is:" + this.getState());
            o.notify();
            System.out.println(this.getName() + "in run  synchronized state2 is:" + this.getState());

        }
        System.out.println(this.getName() + "in run  state2 is:" + this.getState());
    }
}
