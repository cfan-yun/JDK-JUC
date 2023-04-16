package com.cyfan.study.a12.mycase02.activeobject.task;

public class CopyRequest implements Runnable {

    private  final String content;

    public CopyRequest(String content) {
        this.content = content;
    }


    @Override
    public void run() {
        System.out.println("ExecutorThread [" + Thread.currentThread().getName() + "], --->" + content);
        //睡眠
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
