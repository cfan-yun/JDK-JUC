package com.cyfan.study.a02.locks.aqs.b02.share.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 50个人去饭店用餐
 */
public class MySemaphoreTest {

    public static void main(String[] args) {
        MySemaphore semaphore = new MySemaphore(4);
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    semaphore.acquire();//加锁
                    System.out.println(finalI + "号客人进来用餐");
                    Thread.sleep(1000);//用餐时间
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    System.out.println(finalI + "号客人用餐完毕，请下一位客人继续用餐！");
                    semaphore.release();//解锁
                }
            }).start();
        }
    }
}
