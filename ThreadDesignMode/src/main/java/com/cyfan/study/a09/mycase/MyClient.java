package com.cyfan.study.a09.mycase;

/**
 * 模拟客户端
 */
public class MyClient {

    private final MyServer server;
    public MyClient(MyServer server){
        this.server = server;
    }
    public Product request(int count, char c){
        return server.request(count,c);
    }
}
