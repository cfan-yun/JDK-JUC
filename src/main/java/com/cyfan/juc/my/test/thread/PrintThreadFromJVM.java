package com.cyfan.juc.my.test.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrintThreadFromJVM {

    public static void main(String[] args) {

        //获取线程栈
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        Set<Thread> threads = allStackTraces.keySet();
        for (Thread thread : threads) {
            System.out.println(">>>>线程名称：" + thread.getName());
        }
        //debugger 模式执行会比run模式执行少一个线程>>>>线程名称：Monitor Ctrl-Break
        //Monitor Ctrl-Break线程的作用是：防止死锁。监控线程的活动，当出现死锁把线程往外dump的时候，该线程能提供一些信息，提供一些诊断信息
        System.out.println(">>>>>>>>>>>end");
    }
}
