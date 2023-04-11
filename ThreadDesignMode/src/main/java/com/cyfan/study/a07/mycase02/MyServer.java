package com.cyfan.study.a07.mycase02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 自定义服务器
 */
public class MyServer {

    private final int port;

    public MyServer(int port) {
        this.port = port;
    }


    public void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("start listener on " + this.port);
        try {
            while (true) {
                System.out.println("accepting request ..... ");
                Socket socket = serverSocket.accept();
                System.out.println("connect to " + socket);
                new Thread(() -> {
                    try {
                        MyService.service(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //业务逻辑处理
                }).start();

            }
        } finally {
            serverSocket.close();
        }

    }


}
