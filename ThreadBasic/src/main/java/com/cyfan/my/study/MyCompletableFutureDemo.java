package com.cyfan.my.study;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyCompletableFutureDemo {
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(5,10,3L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static void main(String[] args) {
        //testPrint(); //为什么这里能打印
        testNoPrint(); //为什么这里不能打印

    }

    private static void testPrint() {
        long beginTime = System.currentTimeMillis();
        //商品， 库存------》订单--------------》订单明细

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String ret = "任务名称：获取商品信息，执行时间 2000ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        },EXECUTOR);

        //库存
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String ret = "任务名称：获取库存信息，执行时间 1500ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        }, EXECUTOR);

        //订单依赖商品和库存
        cf1.thenCombineAsync(cf2, (s1, s2) ->{
            System.out.println(">>>>>>>>>>>>>>>截至库存和商品执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String ret = "任务名称：订单信息，执行时间 500ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        },EXECUTOR).thenApplyAsync(s -> {
            System.out.println(">>>>>>>>>>>>>>>截至订单执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String ret = "任务名称：订单明细信息，执行时间 600ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        },EXECUTOR).thenAccept(s ->{
            System.out.println(">>>>>>>>>>>>>>>截至订单明细执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");

        }).whenComplete((aVoid, throwable) ->{
            EXECUTOR.shutdown();
        });
    }

    private static void testNoPrint() {
        long beginTime = System.currentTimeMillis();
        //商品， 库存------》订单--------------》订单明细

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String ret = "任务名称：获取商品信息，执行时间 2000ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        });

        //库存
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String ret = "任务名称：获取库存信息，执行时间 1500ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        });

        //订单依赖商品和库存
        cf1.thenCombineAsync(cf2, (s1, s2) ->{
            System.out.println(">>>>>>>>>>>>>>>截至库存和商品执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String ret = "任务名称：订单信息，执行时间 500ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        }).thenApplyAsync(s -> {
            System.out.println(">>>>>>>>>>>>>>>截至订单执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String ret = "任务名称：订单明细信息，执行时间 600ms,线程名：" + Thread.currentThread().getName();
            System.out.println(ret);
            return ret;
        }).thenAccept(s ->{
            System.out.println(">>>>>>>>>>>>>>>截至订单明细执行完花了：" + (System.currentTimeMillis()  - beginTime) + "ms");

        }).whenComplete((aVoid, throwable) ->{
            EXECUTOR.shutdown();
        });
    }

}
