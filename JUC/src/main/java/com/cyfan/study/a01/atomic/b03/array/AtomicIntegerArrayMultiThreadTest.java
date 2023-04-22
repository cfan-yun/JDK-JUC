package com.cyfan.study.a01.atomic.b03.array;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * *            100个线程，对长度为10 的 AtomicIntegerArray 数组中的元素进行操作
 * *            100个线程，对长度为10 的int数组中的元素进行操作
 * 1.AtomicIntegerArray 是如何获取到数组中的元素地址，进行操作的？？？
 * *        如何建立数组下标（index） 和   对应内存偏移量(offset) 之间的关系
 *              offset = baseOffset + index * scale  //常规计算
 *              offset = baseOffset + index << shift //左移计算
 *              checkedByteOffset(int i) //下标转换为地址
 *              baseOffset =  unsafe.arrayBaseOffset(int[].class);
 *              index = i
 *              scale =  unsafe.arrayIndexScale(int[].class);
 *              shift = = 31 - Integer.numberOfLeadingZeros(scale) //numberOfLeadingZeros 表示将scale 变成二进制位之后，最左边有多少个0 二分法和位运算
 */
public class AtomicIntegerArrayMultiThreadTest {

    //原子类数组，线程安全
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
    //操作数据时加锁，线程安全
    private static Integer[] syncIntegerArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //线程不安全的数组
    private static Integer[] integerArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //atomicIntegerArray
                for (int j = 0; j < 10000; j++) {
                    //atomicIntegerArray
                    atomicIntegerArray.incrementAndGet(j % atomicIntegerArray.length());
                    //syncIntegerArray
                    synchronized (AtomicIntegerArrayMultiThreadTest.class) {
                        syncIntegerArray[j % syncIntegerArray.length] += 1;
                    }
                    //integerArray
                    integerArray[j % integerArray.length] += 1;
                }
                countDownLatch.countDown();
            }).start();

        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.print("atomicIntegerArray = ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.print(atomicIntegerArray.get(i));
            if (i < atomicIntegerArray.length()) {
                System.out.print(",");
            }
        }
        System.out.print("\n");
        System.out.print("syncIntegerArray = ");
        for (int i = 0; i < syncIntegerArray.length; i++) {
            System.out.print(syncIntegerArray[i]);
            if (i < syncIntegerArray.length) {
                System.out.print(",");
            }
        }
        System.out.print("\n");
        System.out.print("integerArray = ");
        for (int i = 0; i < integerArray.length; i++) {
            System.out.print(integerArray[i]);
            if (i < integerArray.length) {
                System.out.print(",");
            }
        }

    }

}
