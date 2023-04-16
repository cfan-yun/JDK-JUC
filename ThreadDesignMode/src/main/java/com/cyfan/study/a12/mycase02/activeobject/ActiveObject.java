package com.cyfan.study.a12.mycase02.activeobject;

import java.util.concurrent.Future;

public interface ActiveObject {

    //打印
    Future<String> printContent(int count , char fillChar);
    //复印
    void copyContent(String  content);
}
