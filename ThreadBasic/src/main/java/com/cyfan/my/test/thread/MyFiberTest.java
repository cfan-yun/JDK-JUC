package com.cyfan.my.test.thread;

import co.paralleluniverse.fibers.Fiber;

import java.util.concurrent.ExecutionException;

public class MyFiberTest {
    private static int FIBER_COUNT = 100000;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        Fiber<Void>[] fibers = new Fiber[FIBER_COUNT];

        for (int i = 0; i < FIBER_COUNT; i++){
            fibers[i] = new Fiber(()-> {

                System.out.println(Fiber.currentFiber()+""+calc());
            });
        }
        for (int i = 0; i < FIBER_COUNT; i++){
            fibers[i].start();
        }

        try {
            for (int i = 0; i < FIBER_COUNT; i++){
                        fibers[i].join(); //等待线程执行完之后统一返回
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private static int calc() {
        int sum = 0;
        for (int i = 0; i < 10000; i++){
            for (int j = 0; j < 200; j++){
                sum+=i;
            }
        }
        return sum;
    }
}
