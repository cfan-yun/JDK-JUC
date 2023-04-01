package com.cyfan.my.test.thread.threadConcurrent.Synchronized.myLock;

public class Test {

    static int COUNT = 500;
    final static MyLock myLock =  new MyLock();
    public static void main(String[] args) {
        Test test = new Test();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                test.test();
            }).start();
        }
    }

    private void test() {

        for (;;){

            try {
                myLock.lock();
                if (COUNT <= 0){
                    break;
                }
                System.out.println("current Thread :" + Thread.currentThread().getName() + ", COUNT = "+ (--COUNT));
            } finally {
                myLock.unlock();
            }
        }
    }
}
