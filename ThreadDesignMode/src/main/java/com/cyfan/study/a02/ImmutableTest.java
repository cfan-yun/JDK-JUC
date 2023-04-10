package com.cyfan.study.a02;

public class ImmutableTest {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Person person = new Person("zhangsan", "zhangzhuang");
            new Thread(new PersonTask(person)).start();
        }
    }
}
