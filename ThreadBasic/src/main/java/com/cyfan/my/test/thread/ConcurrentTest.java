package com.cyfan.my.test.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 验证单线程和多线程执行速度，多线程不一定比单线程快
 * 在count很小时，串行快，count 很大数据量特别大时，并发快，测试时持续加大COUNT值
 *
 * 问题， concurrency()方法中如何获取a 的值，使用callable + FutureTask
 */
public class ConcurrentTest {

    private static long COUNT = 100000L;
    public static void main(String[] args) {
        //并发 多线程， 线程Thread和Main线程
        concurrency();
        //串行，单线程Main
        serial();
        //并发，多线程， 线程Thread和Main线程 ，并获取到Thread线程的返回值
        concurrencyGetReturn();

    }

    private static void concurrency() {

        long start = System.currentTimeMillis();

        Thread thread = new Thread(() -> {
            long a = 0;
            for (long i = 0; i < COUNT; i++) {
                a += 3;
            }
        });

        thread.start(); //启动线程后立即返回，由操作系统调度，main线程继续执行

        long b = 0;
        for (long j = 0; j < COUNT; j++) {
            b++;
        }

        try {
            thread.join(); //等待Thread线程执行返回结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end-start);

    }

    private static void serial() {

        long start = System.currentTimeMillis();

        long a = 0;
        for (long i = 0; i < COUNT; i++) {
            a += 3;
        }

        long b = 0;
        for (long j = 0; j < COUNT; j++) {
            b++;
        }

        long end = System.currentTimeMillis();
        System.out.println(end-start);


    }


    private static void concurrencyGetReturn() {

        long start = System.currentTimeMillis();
        FutureTask<Long> task = new FutureTask<>(() -> {
            long a = 0;
            for (long i = 0; i < COUNT; i++) {
                a += 3;
            }
            return a;
        }); // FutureTask中传入的时Callable接口的实现类

        Thread thread = new Thread(task);
        thread.start(); //启动线程后立即返回，由操作系统调度，main线程继续执行

        long b = 0;
        for (long j = 0; j < COUNT; j++) {
            b++;
        }

        try {
            Long aReturn  = task.get(); //此处会阻塞，类似于thread.join()方法等待thread线程执行完返回，获取到返回值
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end-start);

    }
}
