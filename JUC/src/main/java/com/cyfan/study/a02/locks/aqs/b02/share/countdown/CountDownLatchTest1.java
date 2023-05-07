package com.cyfan.study.a02.locks.aqs.b02.share.countdown;


/**
 * 子线程等待main线程执行之后再执行(main未结束)，子线程执行完之后，再唤醒mian线程执行，线程之间交替执行 需要两个CountDownLatch
 * <p>
 * <p>解析：主线程先处理 -----------》 countDown ---------》await---------------------------》 主线程继续执行
 * <p>            子线程 ----》 await ---------》 子线程继续执行（1000ms） ----》 countdown
 */
public class CountDownLatchTest1 {


    public static void main(String[] args) {

//        CountDownLatch countDownLatchForMain = new CountDownLatch(1);// 1 表示所有子线程等待1个main线程等待countDown
//        CountDownLatch countDownLatchForSubThread = new CountDownLatch(5);// 5 表示，main线程要等待5个子线程执行完之后继续执行

//        MyCountDownLatch countDownLatchForMain = new MyCountDownLatch(1);// 1 表示所有子线程等待1个main线程等待countDown
//        MyCountDownLatch countDownLatchForSubThread = new MyCountDownLatch(5);// 5 表示，main线程要等待5个子线程执行完之后继续执行

        MyCountDownLatchBySynchronized countDownLatchForMain = new MyCountDownLatchBySynchronized(1);// 1 表示所有子线程等待1个main线程等待countDown
        MyCountDownLatchBySynchronized countDownLatchForSubThread = new MyCountDownLatchBySynchronized(5);// 5 表示，main线程要等待5个子线程执行完之后继续执行


        //子线程需要等待Main线程执行之后执行
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName() + "，子线程开始执行....");
                    countDownLatchForMain.await();//等待主线程执行完
                    System.out.println(Thread.currentThread().getName() + "，子线程继续执行....");
                    Thread.sleep(1000);//模拟业务逻辑
                    System.out.println(Thread.currentThread().getName() + "，子线程执行完毕....");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    countDownLatchForSubThread.countDown(); // 子线程countdown 5 个线程都执行完了之后，唤醒主线程继续执行
                }

            },"t"+i).start();
        }


        try {
            System.out.println(Thread.currentThread().getName() + "，正在执行。。");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            countDownLatchForMain.countDown();
        }
        System.out.println(Thread.currentThread().getName()+ "， 执行完毕。。唤醒子线程，等待子线程执行。。。。");
        //主线程需要等待子线程执行完之后，继续执行
        try {
            countDownLatchForSubThread.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+ "，子线程已经执行完毕，主线程继续执行。。。。");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName()+ "，主线程执行完毕。。。。退出");
    }
}
