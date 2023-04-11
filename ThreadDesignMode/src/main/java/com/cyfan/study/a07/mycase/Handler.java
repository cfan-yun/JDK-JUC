package com.cyfan.study.a07.mycase;


/**
 * 具体请求处理逻辑
 */
public class Handler {

    public void handle(int num){
        System.out.println("************ "+num+ "start handle ....");
        //处理请求
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("************ "+num+ "end handle ....");
    }
}
