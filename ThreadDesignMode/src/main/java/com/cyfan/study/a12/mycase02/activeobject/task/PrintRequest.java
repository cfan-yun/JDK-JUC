package com.cyfan.study.a12.mycase02.activeobject.task;

import java.util.concurrent.Callable;

public class PrintRequest implements Callable<String> {

    private final int count;
    private final char fillChar;

    public PrintRequest(int count, char fillChar) {
        this.count = count;
        this.fillChar = fillChar;
    }


    @Override
    public String call() {
        char[] chars = new char[count];
        for (int i = 0; i < count; i++) {
            chars[i] = fillChar;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //System.out.println("ExecutorThread [" + Thread.currentThread().getName() + "], --->" + s.toString());
        return new String(chars);//返回真实的数组
    }
}
