package com.cyfan.study.a10.mycase02;

public class AdviceSystem extends GameSystem{


    private boolean shutdown = false;

    public AdviceSystem(String name, long time) {
        super(name, time);
    }

    @Override
    public void event() {
        if (!shutdown){
            System.out.println("THREAD - "+ super.getName()+"运行中。。。");
        }
    }

    @Override
    public void overEvent() {
        System.out.println("THREAD - "+ super.getName()+"已关闭。。。");
        this.shutdown =  true;
    }
}
