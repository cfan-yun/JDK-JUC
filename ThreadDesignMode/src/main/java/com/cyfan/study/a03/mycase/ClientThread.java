package com.cyfan.study.a03.mycase;


/**
 * 模拟客户端
 */
public class ClientThread extends Thread{

    private  RequestQueue requestQueue;
    private  String name;

    public ClientThread(RequestQueue requestQueue, String name){
        this.requestQueue= requestQueue;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {
            Request request = new Request("request" + i);
            System.out.println(Thread.currentThread().getName()+"put the request "+ request);
            requestQueue.putRequest(request);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
