package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized;

/**
 * synchronized 保证有序性、原子性、可见性
 *
 * 局部变量没有安全性问题
 * 成员变量有安全性问题
 *
 */
public class SynchronizedTest {

    private  int num = 0 ;//成员变量存在安全性问题

    public int getNum() {
        return num;
    }

    public static void main(String[] args) {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        Thread t1 = new Thread(() -> {
            synchronizedTest.printNum(1);

        });

        Thread t2 = new Thread(() -> {
            synchronizedTest.printNum(2);

        });

        Thread t3 = new Thread(() -> {
            int i =  1;
            int j =  1;
            while (true){
                if(i++ == 1){
                    System.out.println(synchronizedTest.getNum());
                }

                if(synchronizedTest.getNum() == 100){
                    if (j++ == 1) {
                        System.out.println("========");
                    }
                }

            }
        });
        t3.start();
        t1.start();
        t2.start();


        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     *当num 为成员变量时，会同时出现
     *      t1.num =  100, t2.num = 100
     *      t1.num =  200, t2.num = 200
     * 原因是，t1和t2交替执行，在未执行println之前,t1先修改num=100，t2后修改num=200，最后t1,t2再执行println 都打印200
     *   同理，t1和t2交替执行，在未执行println之前,t2先修改num=200，t1后修改num=100，最后t1,t2再执行println 都打印100
     *
     *   加了synchronize保证了不同线程执行printNum时有序执行，必须t1执行完之后t2线程再执行，反之亦然
     */
    private  void printNum(int i){
        //int num = 0 ;
        if(i == 1){
            num =  100;
        }else{
            num =  200;
        }

        System.out.println("currentThread = "+Thread.currentThread().getName()+", i = " + i + ", num = "+ num);
    }

}
