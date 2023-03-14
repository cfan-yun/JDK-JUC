package com.cyfan.juc.my.test.thread.uilt;

public class Sleep {

    public static void mySleep(long time){
        long start = System.currentTimeMillis();
        long end;
        do {
            end =  System.currentTimeMillis();
        }while (start+time>=end);
    }
}
