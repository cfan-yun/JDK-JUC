package com.cyfan.juc.my.test.thread.threadCommunication;

/**
 *   面试：sleep和wait的区别
 *   1)sleep是Thread的方法,wait是Object的方法
 *   2)sleep不释放 锁， wait释放锁
 *   3)sleep不需要在同步代码块中执行，wait需要在同步代码块内执行
 *  sleep 方法源码解析(jvm层面)
 *      native Thread.sleep(long millis) -> thread.c -> JVM_Sleep -> jvm.cpp -> JVM_Sleep ->  os::sleep -> park(millis)
 *
 */
public class SleepTest {

    public static void main(String[] args) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
