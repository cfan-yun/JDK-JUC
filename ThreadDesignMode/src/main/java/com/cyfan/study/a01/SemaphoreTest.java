package com.cyfan.study.a01;

import java.util.concurrent.Semaphore;

/**
 * 100个人用餐，餐厅最多8个人同时用餐，多线程执行，但是用餐的只能是8个人
 *
 * 每个线程代表一个人
 *
 */
public class SemaphoreTest {


    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(8);
        Thread[] threads = new Thread[100];
        long start = System.currentTimeMillis();
        // 100个人用餐
        for (int i = 0; i < 100; i++) {
            EatTask eatTask = new EatTask(i,semaphore);//客人吃饭任务
            Thread thread = new Thread(eatTask);
            thread.start();//启动线程
            threads[i] = thread;//保存线程
           // System.out.println(i);
        }

        //main等待100个线程执行完退出再说
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("------spend time  = " + (System.currentTimeMillis() -  start));

    }

    private static class EatTask implements Runnable{

        private final int num;
        private final Semaphore semaphore;

        public EatTask(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore =  semaphore;
        }

        @Override
        public void run() {
            //每个人进来前需要判断当前饭店中有多少个人在里面，如果达到8个就等待
            try {
                this.semaphore.acquire(); // 判断餐厅是否有空位

                eat();//用餐

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                System.out.println(">>>>>>>>>>>>>"+this.num + "号，客人用餐完毕,请下一位进场.....");
                this.semaphore.release(); // 客人离开，餐厅腾出空位
            }


        }

        private void   eat(){
            System.out.println(">>>>>>>>>>>>>"+this.num + "号，客人可以用餐.....");
            //每个人用餐平均花费1 秒
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
