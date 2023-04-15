package com.cyfan.study.a11.mycase;

public class Test {

    public static void main(String[] args) {
        Client client = new Client();
        UserInfo userInfo = new UserInfo(); //公共资源
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(()->{
                client.saveUserInfo(userInfo, finalI); //多线程操作公共资源未做同步处理会出现信息错乱
            }).start();
        }

    }
}
