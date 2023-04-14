package com.cyfan.study.a10.mycase02;

public class SystemState {


    private volatile boolean shutdown = false;
    private volatile static SystemState instance = null;

    private SystemState(){};

    public static SystemState getInstance(){
        if (null == instance){
            synchronized (SystemState.class){
                if (null == instance){
                    instance = new SystemState();
                }
            }
        }
        return  instance;
    }

    public boolean isShutdown(){
        return this.shutdown;
    }

    public void sendShutdownState(){
        this.shutdown = true;
    }


}
