package com.cyfan.study.a04.mycase;

import java.io.IOException;

/**
 * 自动刷新线程
 */
public class AutoFlashThread extends Thread{

    private Screen screen;

    public AutoFlashThread(Screen screen){
        this.screen = screen;
    }


    @Override
    public void run() {
        //定时刷新
        while (true){
            try {
                //触发入库刷新动作
                screen.save();
                //模拟业务的定时刷新
                Thread.sleep(2000);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
