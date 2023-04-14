package com.cyfan.study.a09.mycase03;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Spider {

    public static byte[] grab(String urlStr){
        byte[] bytes = new byte[0];
        try {
            URL url = new URL(urlStr);//获取url访问资源
            //获取输入流
            try (DataInputStream dataInputStream = new DataInputStream(url.openStream())) {
                bytes = new byte[1];
                int index = 0;
                while (true) {
                    //读到最后一行没有数据会抛出EOFException
                    int aByte = dataInputStream.readUnsignedByte();
                    //如果index数组下标超过数组长度则扩容
                    if (bytes.length <= index) {
                        byte[] largeBuffer = new byte[bytes.length * 2];
                        System.arraycopy(bytes, 0, largeBuffer, 0,index);
                        bytes = largeBuffer;
                    }

                    bytes[index++] = (byte) aByte;
                }
            } catch (EOFException e) {
                System.out.println("此处代表读完了，读到最后没有数据会抛异常！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }


    public static IContent grabByObj(String urlStr){
        return new ContentImpl(urlStr);
    }

    public static IContent asyncGrabByObj(String urlStr){
        AsyncContentImpl asyncContent = new AsyncContentImpl();

        new Thread(()->{
            asyncContent.setContent(new ContentImpl(urlStr));
        }).start();
        return asyncContent;
    }



    public static void save2File(String fileName, IContent content){
        byte[] bytes = content.getContent();

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)){
            for (byte aByte : bytes) {
                fileOutputStream.write(aByte);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
