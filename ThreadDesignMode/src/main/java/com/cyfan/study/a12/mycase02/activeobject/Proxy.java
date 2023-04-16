package com.cyfan.study.a12.mycase02.activeobject;

import com.cyfan.study.a12.mycase02.activeobject.task.CopyRequest;
import com.cyfan.study.a12.mycase02.activeobject.task.PrintRequest;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Proxy implements ActiveObject {


    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20));
    public Proxy(){
    }

    @Override
    public Future<String> printContent(int count, char fillChar) {
        //返回结果
        return  threadPoolExecutor.submit(new PrintRequest(count,fillChar));
    }

    @Override
    public void copyContent(String content) {
        threadPoolExecutor.submit(new CopyRequest(content));
    }
}
