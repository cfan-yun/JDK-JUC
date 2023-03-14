package com.cyfan.juc.my.test.thread.lambda;

public class LambdaTest {

    public static void main(String[] args) {
        MyFunction fun  = ()->{
            System.out.println("00000");
        };
        fun.run();
    }
}
