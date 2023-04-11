package com.cyfan.study.a07.mycase;

/**
 * 服务端
 */
public class Server {

    private final Handler handler = new Handler();

    public void request(int num){
        System.out.println("#######"+num+ "start handle ...."); //main线程执行
        //1.不使用线程，串行执行
        //handler.handle(num);

        //2.使用多线程并发执行
        new Thread(()->{        //由new Thread 执行
            handler.handle(num);
        }).start();
        System.out.println("#######"+num+ "start handle ....");
    }
}
