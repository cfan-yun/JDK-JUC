package com.cyfan.my.test.thread.jni.start0;

public class MyThreadJNIRun {

    //这里加载C++源码打包后的库文件
    static {
        System.load("/opt/jdk/c-demo/MyThreadJNI.so");//加载文件全路径
        //System.loadLibrary("");//需添加到环境变量中，然后加载文件名（lib.so文件）即可
    }

    /**
     * 方法入口
     * @param args
     */
    public static void main(String[] args) {
        MyThreadJNIRun myThreadJNIRun = new MyThreadJNIRun();//自定义线程类
        myThreadJNIRun.start();//自定义线程启动方法启动线程，后续调用自定义C++方法
    }

    //调用C++start方法
    public void start(){
        start0();
        return ;
    }

    //这个方法用于后续C++回调Java方法
    public void run(){
        System.out.println("this is java run method!");
    }


    //native方法，这个方法用于java调用C++代码
    private native void start0();
}
