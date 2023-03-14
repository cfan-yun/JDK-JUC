package com.cyfan.juc.my.test.thread.threadState.blocked;


public class MyThreadFroBlocked extends  Thread {
    static Object o  = MyLock.obj;

    /**
     * NEW  //
     * RUNNABLE
     * BLOCKED
     * WAITING
     * TIMED_WAITING
     * TERMINATED
     */
    public MyThreadFroBlocked() {
        System.out.println(this.getName()+"in constructor state is：" + this.getState());
    }

    @Override
    public void run() {
        System.out.println(this.getName()+"in run  state1 is:" + this.getState());
        try {
            synchronized (o){
                System.out.println(this.getName()+"in run synchronized  state1 is:" + this.getState());
                Thread.sleep(5000);
                System.out.println(this.getName()+"in run  synchronized state2 is:" + this.getState());
            }
            System.out.println(this.getName()+"in run  state2 is:" + this.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
