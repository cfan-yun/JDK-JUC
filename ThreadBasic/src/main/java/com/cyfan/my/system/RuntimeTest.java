package com.cyfan.my.system;


/**
 * 获取一些系统参数
 */
public class RuntimeTest {

    public static void main(String[] args) {

        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("cup核数i = " + i);
    }
}


