package com.cyfan.study.a10.mycase02;

import java.util.List;

public class Test {


    public static void main(String[] args) throws InterruptedException {
        System.out.println("服务器已启动！");
        List<Thread> threads = SystemServer.startAllSystem();
        Thread.sleep(5000);

        System.out.println("游戏服务器开始关闭");
        //发送变更状态,并中断线程
        SystemServer.shutdown(threads);
    }
}
