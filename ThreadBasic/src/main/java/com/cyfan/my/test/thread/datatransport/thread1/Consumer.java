package com.cyfan.my.test.thread.datatransport.thread1;

public class Consumer extends Thread {

    private static final int fileCount = 24000; //加static 所有线程共享一份
    private static int fileIndex =  0;//加static 所有线程共享一份

    @Override
    public void run() {

        while (fileIndex < fileCount){
            System.out.println(">>>>线程："+Thread.currentThread().getName()+", 读取"+(++fileIndex)+"号文件>>>>>");
        }

    }
}
