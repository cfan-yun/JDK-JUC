package com.cyfan.juc.my.test.thread.threadapi;

public class ThreadPriorityTest {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (true){
                if(i++ == 100){
                    break;
                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println(">>>>>>>>>>>>>>>>>...." + Thread.currentThread().getName());
            }

        });

        Thread thread2 = new Thread(() -> {
            int i = 0;
            while (true){
                if(i++ == 100){
                    break;
                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("       =======...." + Thread.currentThread().getName());
            }
        });

//        thread1.setPriority(Thread.MAX_PRIORITY);//线程1 优先级较thread2高，操作系统尽量让thread1先执行
//        thread2.setPriority(Thread.MIN_PRIORITY);

        thread1.setPriority(4);//线程1 优先级较thread2高，操作系统尽量让thread1先执行
        thread2.setPriority(5);

        thread2.start();
        thread1.start();

    }
}
