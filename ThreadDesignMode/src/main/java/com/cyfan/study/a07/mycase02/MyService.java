package com.cyfan.study.a07.mycase02;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MyService {

    public static   void service(Socket socket) throws IOException {
            OutputStream outputStream = socket.getOutputStream();//获取输出流

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            try {
                dataOutputStream.writeBytes("HTTP/1.0 200 OK \r\n");
                dataOutputStream.writeBytes("Content-type:text/html\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes("<html><head><title>myTitle</title></head><body>");
                for (int i = 0; i < 10; i++) {
                    dataOutputStream.writeBytes("hello, world!");
                    dataOutputStream.writeBytes("<br>");
                    dataOutputStream.flush();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                dataOutputStream.writeBytes("</body></html>");
            } finally {
                dataOutputStream.close();
                socket.close();
            }
    }
}
