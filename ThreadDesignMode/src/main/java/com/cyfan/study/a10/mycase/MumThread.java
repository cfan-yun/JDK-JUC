package com.cyfan.study.a10.mycase;

public class MumThread extends Thread {
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + "让学生做作业-->");
            StudentThread studentThread = new StudentThread();
            studentThread.setName("Student");
            StudentRunnable studentRunnable = new StudentRunnable();
            studentThread.start();

//            Thread t1 = new Thread(studentRunnable);
//            t1.setName("Student");
//            t1.start();
//            studentRunnable.shutDownRunnable();
//            t1.interrupt();//显示中断

            //mun线程休眠，模拟mun去做其他事情

            Thread.sleep(2000);

            //通知学生可以睡觉了
            System.out.println(Thread.currentThread().getName() + "你去睡觉吧");
            //studentThread.close(); //学生线程该结束业务工作，停止线程了，当Sleep/wait时间长时。无法结束
            studentThread.shutdown();//使用中断解决上述问题
            studentThread.join();
            System.out.println(Thread.currentThread().getName() + "---> 明天继续");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
