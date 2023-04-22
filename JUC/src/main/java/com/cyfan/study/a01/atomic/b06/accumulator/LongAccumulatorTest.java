package com.cyfan.study.a01.atomic.b06.accumulator;

import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;


/**
 *  * LongAccumulator 累加、累乘法等累加算法
 *  * (((IDENTIFY+ACCUMULATE) + ACCUMULATE) + ACCUMULATE)
 *  * (((IDENTIFY*ACCUMULATE) * ACCUMULATE) * ACCUMULATE)
 *  见示例
 */
public class LongAccumulatorTest {
    private static int COUNT = 5; //线程数, 增加线程数后，AtomicLong 性能急剧下降 100000 耗时相差接近80倍
    private static int TIMES = 2;//累加次数

    private static int ACCUMULATE =  2;
    private static int IDENTIFY =  1;

    public static void main(String[] args) {
//        testLongAccumulator(COUNT, TIMES, (x,y) -> x + y, IDENTIFY); //(((IDENTIFY+ACCUMULATE) + ACCUMULATE) + ACCUMULATE)
        testLongAccumulator(COUNT, TIMES, (x,y) -> x * y, IDENTIFY); //(((IDENTIFY*ACCUMULATE) * ACCUMULATE) * ACCUMULATE)
    }

    private static void testLongAccumulator(int count, int times, LongBinaryOperator longBinaryOperator,  long identity) {
        long start = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(count);
        LongAccumulator longAccumulator = new LongAccumulator(longBinaryOperator, identity);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    longAccumulator.accumulate(ACCUMULATE);// 1
                }
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("longAccumulator  value = " + longAccumulator.longValue());//最后获取值（求和的动作）
        System.out.println("longAccumulator  spend time(ms)  = " + (System.currentTimeMillis() - start));

    }
}
