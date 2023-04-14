package com.cyfan.study.a10.mycase02;

import java.util.ArrayList;
import java.util.List;

public class SystemServer {

    static GameSystem[] gameSystems =  {
            new ActivitySystem("游戏活动子系统", 500),
            new AdviceSystem("游戏公告子系统",1000)
    };



    protected static void shutdown(List<Thread> threads) {
        //发送变更状态
        SystemState.getInstance().sendShutdownState();
        //中断
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    protected static List<Thread> startAllSystem() {
        List<Thread> threads = new ArrayList<>();
        for (GameSystem gameSystem : gameSystems) {//两个子系统
            Thread thread = new Thread(new TaskManager(gameSystem));
            thread.start();
            threads.add(thread);
        }

        return threads;


    }
}
