package com.cyfan.study.a10.mycase;

public class StudentRunnable implements Runnable{

    private volatile boolean closeThread = false; //用来停止做作业
    private int i;//作业进度

    @Override
    public void run() {
        try {
            while (!isCloseThread()) {
                System.out.println(Thread.currentThread().getName() + "作业进度" + ++i + "%");
                Thread.sleep(1000000000);//假如这里休眠10个小时，那么就算感知到isCloseThread =  true 那么也不能停止，那么应该如何终止？
            }
        } catch (InterruptedException e) {
            System.out.println("捕捉到了异常，打印之前！！");
            e.printStackTrace();//这个打印是由JVM线程打印的与上一句下一句打印的顺序是错乱的
            System.out.println("捕捉到了异常，打印之后！！");
            throw new RuntimeException(e);
        }finally {
            //学生线程停止做作业，后续操作
            //该操作，在学生线程真正停止之间执行,停止业务逻辑之后，停止(销毁)线程之前
            beforeStop();
        }
    }

    /**
     * 真正销毁线程之前的执行逻辑
     */
    public void beforeStop() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + ">>收拾他作业本");
            Thread.sleep(1000);
            System.out.println(">>洗漱");
            Thread.sleep(1000);
            System.out.println(">>洗漱完成，准备睡觉");
            Thread.sleep(1000);
            System.out.println(">>我今天的作业完成进度" + i + "%");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCloseThread() {
        System.out.println("***isCloseThread : " + Thread.currentThread().getName());
        return closeThread;
    }

    public void close() {
        System.out.println("###setCloseThread : " + Thread.currentThread().getName());
        this.closeThread = true;
    }

    /**
     * 当Student不是Thread 而是一个Runnable 时，此时shutdown失效。因为  interrupt(); 是Thread的方法
     * 那么如果此时需要中断需要在外层执行任务的线程显示的发起中断
     *             StudentThread studentRunnable = new StudentThread();
     *             studentRunnable.setName("Student");
     *             Thread t1 = new Thread(studentRunnable);
     *             t1.start();
     *             studentRunnable.shutDownRunnable()
     *             t1.interrupt();//显示中断
     *
     *
     */
    public  void shutDownRunnable(){
        this.closeThread = true;
        //Thread.currentThread().interrupt(); //这里中断的是外层调用的Mum线程
    }
}
