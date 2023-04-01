package com.cyfan.my.test.thread.datatransport.thread2;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {


    private static final int fileCount = 24000; //加static 所有线程共享一份
    private static int fileIndex = 0;//加static 所有线程共享一份
//    private static AtomicInteger fileIndex = new AtomicInteger();

    @Override
    public void run() {

//        while (fileIndex < fileCount){
//           fileIndex++;
//            System.out.println(">>>>线程："+Thread.currentThread().getName()+", 读取"+(fileIndex)+"号文件>>>>>");
//        }

        //原子类
//        while (fileIndex.get() < fileCount) {
//            fileIndex.getAndAdd(1);
//            System.out.println(">>>>线程：" + Thread.currentThread().getName() + ", 读取" + (fileIndex.get()) + "号文件>>>>>");
//        }

        //synchronized

        while (true) {
            synchronized (this) {
                if(fileIndex >= fileCount)break;

                fileIndex++;
                System.out.println(">>>>线程：" + Thread.currentThread().getName() + ", 读取" + (fileIndex) + "号文件>>>>>");
            }
        }

    }
}
