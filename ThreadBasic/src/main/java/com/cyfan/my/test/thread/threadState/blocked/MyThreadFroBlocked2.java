package com.cyfan.my.test.thread.threadState.blocked;

public class MyThreadFroBlocked2 extends Thread {

    static Object o = MyLock.obj;

    /**
     * NEW  //
     * RUNNABLE
     * BLOCKED
     * WAITING
     * TIMED_WAITING
     * TERMINATED
     */
    public MyThreadFroBlocked2() {
        System.out.println(this.getName() + "in constructor state is：" + this.getState());
    }

    @Override
    public void run() {
        System.out.println(this.getName() + "in run  state1 is:" + this.getState());
        synchronized (o) {
            System.out.println(this.getName() + "in run synchronized  state1 is:" + this.getState());
        }
        System.out.println(this.getName() + "in run  state2 is:" + this.getState());

    }
}
