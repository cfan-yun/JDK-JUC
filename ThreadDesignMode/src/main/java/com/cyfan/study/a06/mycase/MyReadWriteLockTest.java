package com.cyfan.study.a06.mycase;

import java.util.Random;

/**
 * 测试类
 */
public class MyReadWriteLockTest {

    public static void main(String[] args) {
//        getString(5);
        TextData textData = new TextData(10);
        for (int i = 0; i < 200; i++) {
           new ReadThread(textData, "read"+i).start();
        }
        for (int i = 0; i < 2; i++) {
            new WriteThread(textData, "write"+i, getString(5)).start();
        }
    }


    private static String getString(int size){
        String value ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        char[] chars = value.toCharArray();
        Random random = new Random();
        char[] newChars = new char[size];
        for (int i = 0;i < size; i++) {
            newChars[i] = chars[random.nextInt(chars.length)];
        }
        String result = String.valueOf(newChars);
        System.out.println("==============="+String.valueOf(result));
        return  result;
    }
}
