package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

/**
 * CacheLine 伪共享问题测试复现
 * CacheLine 一次性加载一块，变量存储在同一个cacheLine由不同的cup操作该Cacheline中的不同变量时，
 * 由于MESI协议限制，会刷主存，（总线传输）从（L1或主存）重读取，导致伪共享问题，效率变低
 * 解决办法：
 * 1.定义多余变量填充满一个CacheLine，会加快速度
 * 2.通过Class层级padding（JDk8之前推荐）
 * 3.通过注解填充，JDK8以后包含8(需要加虚拟机参数 -XX:-RestrictContended才能确保开启生效)
 */
public class CachePseudoSharedTest {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        //Count count = new Count(); //1.定义多余变量填充满一个CacheLine，不推荐
        //CountB count = new CountB();//2.通过Class层级padding（JDk8之前推荐）
        CountAnnotationPadding count = new CountAnnotationPadding();//3.通过注解填充，JDK8以后包含8(需要加虚拟机参数 -XX:-RestrictContended才能确保开启生效)

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                count.a++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                count.b++;
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - start);

    }

    /**
     * 1.定义多余变量填充满一个CacheLine
     */
    static class Count {
        //一个long 8个字节，我的机器一个CacheLine 64字节
        volatile long a;
        /**
         * 不放开这段代码的时候，a,b在一个CacheLine,多核cpu操作a,b数据时，
         * 产生伪共享问题，同一CacheLine会需要反复的加载，需要执行时间要2-3秒；
         * 加上之后，a,b不在一个CacheLineh会更快,减少重新加载同一CacheLine的次数，只需7百多毫秒
         */
        //long p1,p2,p3,p4,p5,p6,p7; //不同虚拟机可能不适用，jdk8之前可用
        volatile long b;
    }


    /**
     * 2.通过Class层级padding（JDk8之前推荐）
     */
    static class CountA{
        volatile long a;
    }

    static  class CountAPadding extends CountA {
        private long p1,p2,p3,p4,p5,p6,p7;
    }

    static  class CountB extends CountAPadding{
        volatile long b;
    }

    /**
     * 3.通过注解填充，JDK8以后(需要加虚拟机参数 -XX:-RestrictContended才能确保生效)
     */
    static class CountAnnotationPadding {
        //一个long 8个字节，我的机器一个CacheLine 64字节
        volatile long a;
        @sun.misc.Contended  //JDK8
        //@jdk.internal.misc.Contended //JDK11
        volatile long b;
    }
}
