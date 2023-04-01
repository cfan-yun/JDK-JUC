package com.cyfan.study.b02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 安全的ArrayList测试
 *
 *
 *
 */
public class SafeListTest {

    public static void main(String[] args) {

        //safeList();//两线程读写arrayList

        safeListSyn(); //

    }

    private static void safeListSyn() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(integerArrayList);
        new Thread(()->{
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; true; i++) {
                synchronizedList.add(i);
                //此处休眠可能报错，读线程也可能读取到值
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronizedList.remove(0);
            }
        }).start();


        new Thread(()->{
            //synchronized (synchronizedList){//此处加锁，上不加休眠，可能读到，可能卡住
                Iterator<Integer> iterator = synchronizedList.iterator();
                System.out.println(Thread.currentThread().getName());
                while (iterator.hasNext()){
                    Integer next = iterator.next();
                    System.out.println(next);
                }
            //}
        }).start();
        //System.out.println("执行完成");
    }


    /**
     * 执行会卡住或者报错，需要多运行几次
     */
    public static void safeList(){
        ArrayList<Integer> integers = new ArrayList<>();
        new Thread(()->{
            for (int i = 0; true; i++) {
                integers.add(i);
                integers.remove(0);
            }
        }).start();


        new Thread(()->{
            Iterator<Integer> iterator = integers.iterator();
            while (iterator.hasNext()){
                Integer next = iterator.next();
                System.out.println(next);
            }
        }).start();
        //System.out.println("执行完成");
    }

}
