package com.cyfan.study.a12.mycase02.client;

import com.cyfan.study.a12.mycase02.activeobject.Proxy;

public class PrinterClient {

    private final Proxy proxy;
    public PrinterClient() {
        this.proxy = new Proxy();
    }

    public void print(String content){
        new PrintThread(content,proxy).start();//发送一个打印请求
    }



    public void  copy(String content){
        new CopyThread(content, proxy).start();
    }

}
