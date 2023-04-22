package com.cyfan.study.a01.atomic.b02.basic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerTest {

    private static int count =  0;
    public static void main(String[] args) {

        //测试原子类并发修改
        testAtomicThreadCurrent();
        testIntUnaryOperator();
    }

    private static void testAtomicThreadCurrent() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(()->{
            for (int i = 0; i < 50000; i++) {
                count++;
                atomicInteger.getAndIncrement();
            }
            countDownLatch.countDown();
        }).start();

        new Thread(()->{
            for (int i = 0; i < 50000; i++) {
                count++;
                atomicInteger.getAndIncrement();
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(count);
        System.out.println(atomicInteger);
    }


    private static void testIntUnaryOperator(){
        AtomicInteger atomicInteger = new AtomicInteger(100);
        IntUnaryOperator intUnaryOperatorBefore = (x) -> x * 2 ;
        IntUnaryOperator intUnaryOperatorAfter = (x) -> x - 2 ;

        int andUpdate = atomicInteger.getAndUpdate(intUnaryOperatorBefore);
        System.out.println("andUpdate = " + andUpdate);
        int andGet = atomicInteger.updateAndGet(intUnaryOperatorBefore);
        System.out.println("andGet = " + andGet);

        IntUnaryOperator compose = intUnaryOperatorAfter.compose(intUnaryOperatorBefore); // intUnaryOperatorBefore 先执行，intUnaryOperatorAfter后执行
        IntUnaryOperator andThen = intUnaryOperatorBefore.andThen(intUnaryOperatorAfter); // intUnaryOperatorBefore 先执行，intUnaryOperatorAfter后执行
        int composeInt = atomicInteger.updateAndGet(compose);
        System.out.println("composeInt = " + composeInt);
        int andThenInt = atomicInteger.updateAndGet(andThen);
        System.out.println("andThenInt = " + andThenInt);

        //IntUnaryOperator 应用
        IntUnaryOperator self = IntUnaryOperator.identity();
        int selfInt = self.applyAsInt(6);// 返回6
        System.out.println("selfInt = " + selfInt);

        IntUnaryOperator expr1  = t -> t * 3;
        int expr1Ret = expr1.applyAsInt(6);// 6 * 3 = 18
        System.out.println("expr1Ret = " + expr1Ret);


        IntUnaryOperator expr2 =  t -> t * 3;
        IntUnaryOperator expr3 = expr2.andThen(t -> t + 5);// 先执行  t * 3 ->  ( t * 3) + 5
        int expr3Ret = expr3.applyAsInt(6);
        System.out.println("expr3Ret = " + expr3Ret);//23

        IntUnaryOperator expr4 =  t -> t * 3;
        IntUnaryOperator expr5 = expr2.compose(t -> t + 5);// 先执行  t + 5 ->  ( t + 5) * 3
        int expr5Ret = expr5.applyAsInt(6);
        System.out.println("expr5Ret = " + expr5Ret); // 33


        IntBinaryOperator intBinaryOperator = (x,y) -> x + y;
        int intBinaryOperatorRet = intBinaryOperator.applyAsInt(1, 2);


    }
}
