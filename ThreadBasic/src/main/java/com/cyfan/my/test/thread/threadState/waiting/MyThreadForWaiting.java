package com.cyfan.my.test.thread.threadState.waiting;

public class MyThreadForWaiting extends Thread{

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
    public MyThreadForWaiting() {
        System.out.println(this.getName() + "in MyThreadForWaiting state is：" + this.getState());
    }

    @Override
    public void run() {
        System.out.println(this.getName() + "in run  state1 is:" + this.getState());
        synchronized (o){
            try {
                //o.notify();
                System.out.println(this.getName() + "in run  synchronized state1 is:" + this.getState());
                o.wait();
                System.out.println(this.getName() + "in run  synchronized state2 is:" + this.getState());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.getName() + "in run  state2 is:" + this.getState());
    }
}
