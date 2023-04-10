package com.cyfan.study.a04.mycase02;

/**
 * 模拟服务器初始化的测试类
 */
public class ServerInitTest {

    public static void main(String[] args) {
        ServerInit serverInit = new ServerInit();
        new Thread(()->{
            serverInit.init();
        }).start();

    }
}
