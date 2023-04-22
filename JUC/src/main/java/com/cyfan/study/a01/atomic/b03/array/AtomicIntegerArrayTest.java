package com.cyfan.study.a01.atomic.b03.array;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayTest {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        printAtomicArrayValue(atomicIntegerArray, "1");
        System.out.println("=================");
        atomicIntegerArray.set(2, 10);//设置下标位置2 的值为10
        printAtomicArrayValue(atomicIntegerArray, "2");

        atomicIntegerArray.addAndGet(2, 5);//在下标为2的位置上+5
        printAtomicArrayValue(atomicIntegerArray, "3");

        boolean b = atomicIntegerArray.compareAndSet(2, 15, 25);//将下标为2 的位置修改为25 预期值是15
        System.out.println("b = " + b);
        printAtomicArrayValue(atomicIntegerArray, "4");

        boolean b1 = atomicIntegerArray.compareAndSet(2, 15, 30);//将下标为2 的位置修改为25 预期值是15
        System.out.println("b1 = " + b1); // 修改失败
        //printAtomicArrayValue(atomicIntegerArray, "5");

        atomicIntegerArray.incrementAndGet(2); //2 数组下标上执行++1操作
        printAtomicArrayValue(atomicIntegerArray, "5");

    }

    private static void printAtomicArrayValue(AtomicIntegerArray atomicIntegerArray, String per) {
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            int indexValue = atomicIntegerArray.get(i);
            System.out.println(per +" [index = " + i + ", indexValue = " + indexValue + "]");// 10个0
        }
    }
}
