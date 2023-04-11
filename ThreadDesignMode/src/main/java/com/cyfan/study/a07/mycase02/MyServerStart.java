package com.cyfan.study.a07.mycase02;

import java.io.IOException;

/**
 * 使用浏览器调用
 */
public class MyServerStart {

    public static void main(String[] args) {
        MyServer myServer = new MyServer(8888);
        try {
            myServer.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
