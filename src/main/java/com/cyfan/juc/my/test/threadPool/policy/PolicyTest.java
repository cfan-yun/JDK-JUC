package com.cyfan.juc.my.test.threadPool.policy;

import com.cyfan.juc.my.test.thread.uilt.Sleep;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 拒绝策略验证
 */
public class PolicyTest {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2));
        //threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //1.中止策略--抛出异常
        //threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //2.调用者执行策略--main线程调用线程池，main线程执行
        //threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy()); // 3.丢弃最旧的任务
        //threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//4.直接拒绝--无任何提示
        threadPoolExecutor.setRejectedExecutionHandler(new MyPolicy());//5.自定义的拒绝策略--打印队列大小，线程池中活跃线程数，并开启新的线程执行

        //1.如果 任务数 < 核心线程数，核心线程处理
//        for (int i = 0; i < 3; i++) {
//            MyTask myTask = new MyTask(i);
//            threadPoolExecutor.execute(myTask);
//            System.out.println("任务队列中任务个数："+threadPoolExecutor.getQueue().size());
//        }

        // 2. 如果 任务数=核心线程数+队列数，
//        for (int i = 0; i < 3; i++) {
//            MyTask myTask = new MyTask(i);
//            threadPoolExecutor.execute(myTask);
//            System.out.println("任务队列中任务个数："+threadPoolExecutor.getQueue().size());
//        }

        //3.任务数>核心线程数+最大队列数； 6个任务：核心线程执行3个任务，队列放两个，普通线程执行1个
//        for (int i = 0; i < 6; i++) {
//            MyTask myTask = new MyTask(i);
//            threadPoolExecutor.execute(myTask);
//            System.out.println("任务队列中任务个数："+threadPoolExecutor.getQueue().size());
//        }


        //4.任务数>核心线程数+最大队列数 + 普通线程数； 20个任务：核心线程执行3个任务，队列放2个，普通线程执行7个，剩余7个被拒绝
        /**
         * 分析：核心线程数3， 3个线程启动，剩余20-3 = 17个任务
         * 此时有10个任务需要进队列，队列中进了2个， 17 -2 = 15个任务
         * 普通线程数7， 此时，就算7个线程全部启动，那么也会剩余8个任务无法处理，
         * 此时就会触发拒绝策略
         */
        for (int i = 0; i < 20; i++) {
            MyTask myTask = new MyTask(i);
            threadPoolExecutor.execute(myTask);
            if (i == 0){
                System.out.println(i+"任务队列中任务个数："+threadPoolExecutor.getQueue().size());
            }else if (i == 3){
                System.out.println(i+"任务队列中任务个数："+threadPoolExecutor.getQueue().size());
            }else if (i ==  5){
                System.out.println(i+"任务队列中任务个数："+threadPoolExecutor.getQueue().size());
            }else if(i == 10) {
                System.out.println(i+"任务队列中任务个数："+threadPoolExecutor.getQueue().size());
            }

        }



    }
}
