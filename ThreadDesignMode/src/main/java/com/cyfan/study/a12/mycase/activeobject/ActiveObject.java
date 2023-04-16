package com.cyfan.study.a12.mycase.activeobject;

import com.cyfan.study.a12.mycase.activeobject.result.Result;

public interface ActiveObject {

    //打印
    Result<String> printContent(int count , char fillChar);
    //复印
    void copyContent(String  content);
}
