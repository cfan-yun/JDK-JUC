package com.cyfan.study.a10.mycase02;

public abstract  class GameSystem {

    private String name;
    private long time;


    public GameSystem(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public abstract void event();

    public abstract void overEvent();

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
