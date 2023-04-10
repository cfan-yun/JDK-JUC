package com.cyfan.study.a03.mycase;

/**
 * 使用的设计模式：
 *      1、有synchronized关键字，说明使用了SingleThreadExecute 单线程执行设计模式
 *      2、request类属性使用final修饰，使用了Immutable 不可变模式
 *      3、在RequestQueue中，wait() 挂起消费者线程，使用了GuardedSuspension 保护性暂挂模式
 */
public class MyServer {

    public static void main(String[] args) {

        RequestQueue requestQueue = new RequestQueue();
        ClientThread clientThread = new ClientThread(requestQueue, "client");
        ServerThread serverThread = new ServerThread(requestQueue, "server");
        clientThread.start();
        serverThread.start();

    }
}
