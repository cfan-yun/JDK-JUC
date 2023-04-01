package com.cyfan.study.a01;


/**
 * 模式1：SingleThreadExecution 模式
 * pass方法进行了1此写操作，2次读操作
 *
 * 需要加锁在pass和toString 这两个public方法
 */
public class SingleThreadTest {

    public static void main(String[] args) {
        SafeGate safeGate = new SafeGate();
        //zhangsan zhangzhuang
        Thread thread1 = new Thread(() -> {
            System.out.println("zhangsan come in ");
            while (true){
                safeGate.pass("zhangsan", "zhangzhuang");
                //System.out.println(safeGate.toString());
            }
        });


        //lisi lizhuang
        Thread thread2 = new Thread(() -> {
            System.out.println("lisi come in ");
            while (true){
                safeGate.pass("lisi", "lizhuang");
                //System.out.println(safeGate.toString());

            }

        });


        //wangwu wanghzuang
        Thread thread3 = new Thread(() -> {
            System.out.println("wangwu come in ");
            while (true){
                safeGate.pass("wangwu", "wanghzuang");
                //System.out.println(safeGate.toString());
            }

        });

        Thread thread11 = new Thread(() -> {
            System.out.println("zhangsan come in ");
            while (true){
                safeGate.toString();
                //System.out.println(Thread.currentThread().getName() + safeGate.toString());
            }
        });


        //lisi lizhuang
        Thread thread22 = new Thread(() -> {
            System.out.println("lisi come in ");
            while (true){
                safeGate.toString();
                //System.out.println(Thread.currentThread().getName() + safeGate.toString());

            }

        });


        //wangwu wanghzuang
        Thread thread33 = new Thread(() -> {
            System.out.println("wangwu come in ");
            while (true){
                safeGate.toString();
                //System.out.println(Thread.currentThread().getName() + safeGate.toString());
            }

        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread11.start();
        thread22.start();
        thread33.start();

    }

}
