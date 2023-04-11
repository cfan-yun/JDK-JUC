package com.cyfan.study.a06.mycase;

import java.util.Random;

public class ReadThread extends Thread {

    private final TextData textData;
    private static final Random RANDOM = new Random();

    public ReadThread(TextData textData, String name) {
        super(name);
        this.textData = textData;
    }

    @Override
    public void run() {
        while (true) {
            try {
                char[] readBuf = textData.read();//读
                System.out.println(Thread.currentThread().getName() + ", read " + String.valueOf(readBuf));
                Thread.sleep(RANDOM.nextInt(3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
