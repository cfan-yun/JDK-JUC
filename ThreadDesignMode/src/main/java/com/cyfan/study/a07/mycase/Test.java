package com.cyfan.study.a07.mycase;

/**
 * 模拟客户端调用服务端
 */
public class Test {

    public static void main(String[] args) {
        Client client = new Client();
        for (int i = 0; i < 4; i++) {
            client.request(i);
        }
    }
}
