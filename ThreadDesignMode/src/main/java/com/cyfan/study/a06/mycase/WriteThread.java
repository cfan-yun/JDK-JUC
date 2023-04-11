package com.cyfan.study.a06.mycase;

import java.util.Random;

public class WriteThread extends Thread {

    private final TextData textData;
    private final String filler;
    private int index;

    private static final Random RANDOM = new Random();

    public WriteThread(TextData textData, String name, String filler) {
        super(name);
        this.textData = textData;
        this.filler = filler;
    }

    @Override
    public void run() {
        while (true) {
            try {

                char c = nextChar();
                textData.write(c);//写
                System.out.println(Thread.currentThread().getName() + ", write " + c);
                //休眠
                Thread.sleep(RANDOM.nextInt(3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private char nextChar() {
        char c = this.filler.charAt(index);
//        index++;
//        if (index >= this.filler.length()){
//            index =  0;
//        }
        index = (index + 1) % this.filler.length();
        return c;
    }
}
