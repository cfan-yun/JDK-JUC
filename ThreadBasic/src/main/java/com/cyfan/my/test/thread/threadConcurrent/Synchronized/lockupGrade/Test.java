package com.cyfan.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

public class Test {
    static int  COUNT = 100;
//    Object obj =  new Object();
    final  static  MySynchronized mySynchronized = new MySynchronized();
    public static void main(String[] args) {
        Test test = new Test();
        for (int i = 0; i < 16; i++) {
            new Thread(()->{
                test.test1();
            }).start();
        }

    }

    public void test1(){
        for (;;){

//            synchronized (obj){
//            }
            try {
                mySynchronized.monitorEnter();
                if (COUNT <= 0){
                    break;
                }
                System.out.println("current thread :" + Thread.currentThread().getName() + ", count:"+ --COUNT);
            }finally {
                mySynchronized.monitorExit();
            }


        }
    }
}
