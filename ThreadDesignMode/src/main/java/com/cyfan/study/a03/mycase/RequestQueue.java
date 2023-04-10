package com.cyfan.study.a03.mycase;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {

    private final Queue<Request> queue =  new LinkedList<>();
    //使用线程安全的queue
    private final Queue<Request> blockingQueue =  new LinkedBlockingQueue<>(100);

    /**
     * 生产者执行
     * @param request
     */
    public  void putRequest(Request request){

        synchronized (this){
            queue.offer(request);
            this.notifyAll();
        }

        //blockingQueue.offer(request); //使用线程安全的queue
    }


    /**
     * 消费者执行
     * @return
     */
    public Request getRequest(){
        //保护性暂挂核心
        synchronized (this){
            while (queue.isEmpty()){ //不能使用if会导致虚假唤醒
                try {
                    this.wait();//线程卡死等待，线程暂时挂起，暂时挂起消费者线程
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            return queue.remove();
        }

        //return (Request) blockingQueue.remove(); //使用线程安全的queue
    }


}
