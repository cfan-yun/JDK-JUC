package com.cyfan.study.a03.mycase;

/**
 * 模拟服务端
 */
public class ServerThread extends Thread{

    private  String name;

    private RequestQueue requestQueue;

    public ServerThread(RequestQueue requestQueue, String name){
        this.requestQueue = requestQueue;
        this.name = name;
    }


    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {
            Request request = requestQueue.getRequest();
            System.out.println(Thread.currentThread().getName()+ "处理请求"+request.getName());
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
