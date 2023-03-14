package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

import java.util.ArrayList;

/**
 * 共享变量进行线程间通信测试
 *  想要测试的问题：
 *      消费者线程中，让list中保存了j变量的由1->count的值，先start。
 *      生产者线程中，2秒后让共享变量i的值改变由1->count
 *      此时想要查看当list.contains(i) 时打印i值，会发现当count小的时候，无法打印，逐渐变大后，
 *      达到某个临界值之后(本机是504就打印了，较小时，打印值和count相等)，count很大时，就随机打印了
 */
public class VariableShareTest1 {

    static int i = 0; //共享变量
    static int count = 100000; //设置100， 200， 300 ....1000,10000,100000,查看结果

    public static void main(String[] args) {
        new Thread(() -> {
            ArrayList<Object> list = new ArrayList<>();
            //list中保存1->count的数据
            for (int j = 1; j <= count; j++){
                list.add(j);
            }
            while (true){
                if(list.contains(i)){
                    System.out.println(">>>>>>>线程消费开始，i = "+i);
                    break;
                }
            }

        }, "consumer").start();

        new Thread(() -> {
            //增加休眠方法后，consumer线程获取不到i 最新值
            mySleep(2000000000);
            //2秒后i变量被赋值逐渐由1 -> count
            for (int x = 1; x <= count; x++){
                i = x;
            }
        }, "producer").start();

    }


    static void mySleep(long time) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
            ;
        } while (start + time >= end);
    }
}
