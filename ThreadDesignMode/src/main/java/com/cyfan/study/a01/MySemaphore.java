package com.cyfan.study.a01;

public class MySemaphore {

    private  int count;

    public MySemaphore(int count){
        this.count = count;
    }

    public void acquire() throws InterruptedException {
        synchronized (this){
            while (true){
                if (this.count >0){
                    this.count--;
                    break;
                }
                this.wait();
            }
        }
    }

    public void  release(){
        synchronized (this){
            this.count++;
            this.notifyAll();
        }
    }
}
