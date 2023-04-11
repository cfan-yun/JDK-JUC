package com.cyfan.study.a07.mycase;


/**
 * 客户端
 */
public class Client {

    private  final  Server server = new Server();

    public void request(int num){
        server.request(num);
    }
}
