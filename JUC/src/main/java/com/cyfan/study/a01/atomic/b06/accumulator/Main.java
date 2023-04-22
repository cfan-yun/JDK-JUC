package com.cyfan.study.a01.atomic.b06.accumulator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 1.8 出现的Adder 因为什么原因出现的？？
 * *      AtomicLong 原理其实是cas+自旋 （线程数急剧增大的时候，会导致性能急剧下降）
 * *      LongAdder  为解决减少自旋带来的性能消耗而出现（以空间换时间，减少自旋）
 * *      longAdder.increment();
 */
public class Main {



}
