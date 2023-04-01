package com.cyfan.my.test.thread.threadapi;

public class DaemonThreadCase {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread());
    }
}
