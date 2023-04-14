package com.cyfan.study.a09.mycase02;

/**
 * 测试类
 */
public class Test {


    public static void main(String[] args) throws InterruptedException {
        MyServer myServer = new MyServer();
        MyClient myClient = new MyClient(myServer);
        System.out.println("############发送请求开始.............");
        Product a = myClient.request(5, 'A');
        Product b = myClient.request(5, 'B');
        Product c = myClient.request(5, 'C');
        Product d = myClient.request(5, 'D');
        Product e = myClient.request(5, 'E');

        System.out.println("=================刚发送完就获取异步结果start=====================");
//        System.out.println("RealProduct====="+a.getContent());
//        System.out.println("RealProduct====="+b.getContent());
//        System.out.println("RealProduct====="+c.getContent());
//        System.out.println("RealProduct====="+d.getContent());
//        System.out.println("RealProduct====="+e.getContent());

        System.out.println("=================刚发送完就获取异步结果end=====================");
        //这里可以做其他事情
        System.out.println("############发送完请求之后做其他事情.............start");
        //模拟业务耗时
        Thread.sleep(2000);
        System.out.println("############发送完请求之后做其他事情.............end");
        System.out.println("RealProduct====="+a.getContent());
        System.out.println("RealProduct====="+b.getContent());
        System.out.println("RealProduct====="+c.getContent());
        System.out.println("RealProduct====="+d.getContent());
        System.out.println("RealProduct====="+e.getContent());

        System.out.println("############发送请求结束.............");
    }
}
