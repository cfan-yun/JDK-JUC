package com.cyfan.study.a06.mycase02;


/**
 * 测试类
 */
public class Test {

    public static void main(String[] args) {

        MyDataBase<String, String> dataBase = new MyDataBase<>();
        new WriteDataBaseThread<String, String>(dataBase,"zhangsan", "zhangzhuang").start();
        new WriteDataBaseThread<String, String>(dataBase,"zhangsan", "zz").start();
        new WriteDataBaseThread<String, String>(dataBase,"lisi", "lizhuang").start();
        new WriteDataBaseThread<String, String>(dataBase,"lisi", "lz").start();
        new WriteDataBaseThread<String, String>(dataBase,"wanger", "wangzhuang").start();
        new WriteDataBaseThread<String, String>(dataBase,"wanger", "wz").start();
        new WriteDataBaseThread<String, String>(dataBase,"zhuwu", "zhuzhuang").start();

        for (int i = 0; i < 100; i++) {
            new ReadDataBaseThread<String,String>(dataBase, "zhangsan").start();
            new ReadDataBaseThread<String,String>(dataBase, "lisi").start();
            new ReadDataBaseThread<String,String>(dataBase, "wanger").start();
        }
    }
}
