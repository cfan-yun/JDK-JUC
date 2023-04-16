package com.cyfan.study.a12.mycase.client;

import com.cyfan.study.a12.mycase.activeobject.ActiveObject;

public class CopyThread extends Thread{

    public CopyThread(String name,ActiveObject proxy) {
        super(name);
        this.proxy = proxy;
    }

    private final ActiveObject proxy;

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                String content = Thread.currentThread().getName() +  "***复印内容：" + Thread.currentThread().getName() + "第" + i + "份";
                //System.out.println(content);
                //发起复印
                proxy.copyContent(content);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
