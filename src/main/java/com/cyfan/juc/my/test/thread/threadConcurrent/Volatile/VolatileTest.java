package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

/**
 * 测试复现cup乱序导致的多线程之间共享变量可见性问题、指令重排问题
 * 原因是cpu和L1 Cache之间存在一个store buffer的原因，store buffer 本质上是一个异步队列
 */
public class VolatileTest {

    private static int a = 0, b = 0;
    private static int x = 0, y = 0;

    public static void main(String[] args) {

        for (int i = 1; ; i++) { //无限循环
            //初始化
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            //启两线程
            Thread t1 = new Thread(() -> {
                a = 1;
                //mySleep(1000);
                x = b;
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                //mySleep(1000);
                y = a;
            });

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = "第" + i + "次，(x,y)= (" + x + "," + y + ")";

            /**
             * 存在四种情况：
             * 1、t1先执行完，t2后执行完
             *   x = 0, y = 1;
             * 2、t2先执行完，t1后执行完
             *   x = 1, y = 0;
             * 3、t1,t2交替执行，t1，t2，都先把a = 1, b = 1 执行完了
             *   x = 1, y = 1;
             * 4、x = 0， y = 0 是发生了指令重排，  x = b; y = a; 排前面去了
             *      因为早期MESI协议约束，缓存一致性约束强，其他cpu繁忙时，当前处理线程的cpu响应，造成当前等待，浪费资源。
             *          然后引入了store buffer，使各个cpu store写的时候异步操作了，导致了乱序。
             */

            if (x == 0 && y == 0) {
                System.out.println(result);
                //break;
            } else if (x == 1 && y == 1) {
                System.out.println(result);
                break;
            } else {

                if(i % 1000 == 0){
                    System.out.println(result);
                }

            }


        }
    }

    static void mySleep(long time){
        long start = System.currentTimeMillis();
        long end;
        do {
            end =  System.currentTimeMillis();
        }while (start+time>=end);
    }

}
