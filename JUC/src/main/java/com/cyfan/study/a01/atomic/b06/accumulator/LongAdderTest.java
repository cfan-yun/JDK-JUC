package com.cyfan.study.a01.atomic.b06.accumulator;

import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;


/**
 * 1.8 出现的Adder 因为什么原因出现的？？
 * *      AtomicLong 原理其实是cas+自旋 （线程数急剧增大的时候，会导致性能急剧下降）
 * *      LongAdder  为解决减少自旋带来的性能消耗而出现（以空间换时间，减少自旋）
 * *      longAdder.increment();
 */
public class LongAdderTest {
    private static int COUNT = 100000; //线程数, 增加线程数后，AtomicLong 性能急剧下降 100000 耗时相差接近80倍
    private static int TIMES = 100000;//累加次数

    public static void main(String[] args) {

        testAtomicLong(COUNT, TIMES);

        testLongAdder(COUNT, TIMES);

//        testGetProbe();
    }

    private static void testGetProbe() {

        int length = 4;
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {

                    synchronized (LongAdderTest.class) {
                        int probe = getProbe();
                        String name = Thread.currentThread().getName();
                        System.out.println(name + ", probe = " + probe);
                        ThreadLocalRandom.current();
                        int probe1 = getProbe();
                        System.out.println(name + ", probe1 = " + probe1);
                        int probe2 = advanceProbe(probe);
                        System.out.println(name + ", probe -> probe2 = " + probe2);

                        int probe3 = advanceProbe(probe1);
                        System.out.println(name + ", probe1 -> probe3 = " + probe3);

                        int index = (length - 1) & probe;
                        int index1 = (length - 1) & probe1;
                        int index2 = (length - 1) & probe2;
                        int index3 = (length - 1) & probe3;
                        System.out.println("index = " + index);
                        System.out.println("index1 = " + index1);
                        System.out.println("index2 = " + index2);
                        System.out.println("index3 = " + index3);
                    }

                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }


    }

    private static int advanceProbe(int probe) throws NoSuchFieldException {
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long PROBE = unsafe.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
        probe ^= probe << 13;   // xorshift
        probe ^= probe >>> 17;
        probe ^= probe << 5;
        unsafe.putInt(Thread.currentThread(), PROBE, probe); // Thread 当前线程的threadLocalRandomProbe 字段修改为 probe
        return probe;
    }

    /**
     * 获取当前线程的hash 值
     *
     * @throws NoSuchFieldException 反射异常
     */
    private static int getProbe() throws NoSuchFieldException {
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long prob = unsafe.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
        int hash = unsafe.getInt(Thread.currentThread(), prob);
        return hash;
    }

    private static void testAtomicLong(int count, int times) {

        long start = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(count);
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    atomicLong.incrementAndGet(); //i = i + 1
                }
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("AtomicLong  value = " + atomicLong.get());
        System.out.println("AtomicLong  spend time(ms)  = " + (System.currentTimeMillis() - start));
    }

    private static void testLongAdder(int count, int times) {
        long start = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(count);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    longAdder.increment();// 1
                }
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("LongAdder  value = " + longAdder.longValue());//最后获取值（求和的动作）
        System.out.println("LongAdder  spend time(ms)  = " + (System.currentTimeMillis() - start));

    }
}
