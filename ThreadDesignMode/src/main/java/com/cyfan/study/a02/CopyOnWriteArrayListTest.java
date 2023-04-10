package com.cyfan.study.a02;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {

        //写线程
        final CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; true; i++) {
                copyOnWriteArrayList.add(i);
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                copyOnWriteArrayList.remove(0);
            }
        }).start();


        new Thread(()->{
            Iterator<Integer> iterator = copyOnWriteArrayList.iterator();
            System.out.println(Thread.currentThread().getName());
            while (iterator.hasNext()){
                Integer next = iterator.next();
                System.out.println(next);
            }
        }).start();


    }
}
