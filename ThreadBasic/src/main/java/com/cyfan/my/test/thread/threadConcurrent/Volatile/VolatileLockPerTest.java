package com.cyfan.my.test.thread.threadConcurrent.Volatile;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 测试复现cup乱序导致的多线成之间共享变量可见性问题、指令重排问题
 * 原因是cpu和L1 Cache之间存在一个store buffer的原因，store buffer 本质上是一个异步队列
 *
 * 如何解决呢？
 *      可以加读写屏障
 *      x84 下只存在store load 屏障(写读屏障)
 *
 *      stroestroe  写写屏障，------x86的写屏障默认是空操作，因为x86规定，所有的写操作都必须到stroebuffer中排队
 *      loadload    读读屏障，------x86 没有invalidate queue
 *      loadstroe   读写屏障，------x86 中没有，防止写操作，在读操作之前执行，x86可以保证读写重排序
 *      stroeload   写读屏障，下面案例就是典型的写读重排序
 *
 * volatile 底层也是LOCK前缀 由于存在LOCK前缀，会锁CacheLine，刷新store buffer ，还会刷新Cache到内存。
 * LOCK在前期的MESI 协议中是锁总线的，但是由于效率太低后面改锁CacheLine了。
 *
 */
public class VolatileLockPerTest {

    private static int a = 0, b = 0;
    private static int x = 0, y = 0;


    public static Unsafe getUnsafe(){
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

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
                //写屏障，实际上这里写屏障并未生效，生效的是反射(getDeclaredField)的CAS底层源码中的LOCK前缀 和sychronized 无关
                //VolatileLockPerTest.getUnsafe().storeFence();
                try {
                    Unsafe.class.getDeclaredField("theUnsafe");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                //mySleep(1000);
                x = b;//两个操作，读b与写x
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                //写屏障，实际上这里写屏障并未生效，生效的是反射的CAS底层源码中的LOCK前缀
                try {
                    Unsafe.class.getDeclaredField("theUnsafe");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
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
                //break;
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
