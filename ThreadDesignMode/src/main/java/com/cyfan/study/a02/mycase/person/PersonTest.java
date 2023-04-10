package com.cyfan.study.a02.mycase.person;

public class PersonTest{

    public static void main(String[] args) {
        MutablePerson mutablePerson = new MutablePerson("zhangsan", "zhangsan");
        for (int i = 0; i < 20; i++) {
            new MutablePersonTask(mutablePerson).start();
        }

        for (int i = 0; i < 10000000; i++) {
            //mutablePerson.setName("lisi");
            mutablePerson.setPerson("lisi"+i,"lisi"+i);
        }

    }

}
