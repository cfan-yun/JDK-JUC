package com.cyfan.juc.my.test.thread.safe;

import java.io.IOException;

public class SafeThreadTest {

    public static void main(String[] args) {
//        exitVM();
        delSystemFile();
    }

    /**
     * 测试关闭虚拟机的权限保护
     */
    public static void exitVM() {
        //子线程第三方实现方法关闭虚拟机
        new Thread(new SafeRunnable() {
            @Override
            public void protectMethod() {
                System.out.println("this is safe code!");//安全性代码
                System.exit(0); //退出虚拟机，非安全性代码， 此处不允许执行，关闭安全管理器之后（注释创建安全管理器那两行代码），允许执行

            }
        }).start();

        //main线程休眠
        try {
            Thread.sleep(1000); //睡眠10秒，让子线程退出虚拟机
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试删除系统文件时的权限控制
     */
    public static void  delSystemFile(){
        new Thread(new SafeRunnable() {
            @Override
            public void protectMethod() {
                System.out.println("this is safe code!");//安全性代码
                //在保护域内不允许执行
//                try {
////                    Runtime.getRuntime().exec("notepad.exe"); //启动notepad程序
//                    Runtime.getRuntime().exec("cmd.exe /c del d:\\test.txt /q"); //删除d盘下的test.txt
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();


        //不在保护域内可执行。
        try {
//            Runtime.getRuntime().exec("notepad.exe"); //启动notepad程序
            Runtime.getRuntime().exec("cmd.exe /c del d:\\test.txt /q"); //删除d盘下的test.txt
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

