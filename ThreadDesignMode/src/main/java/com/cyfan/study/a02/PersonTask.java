package com.cyfan.study.a02;

public class PersonTask implements Runnable{
    private Person person;

    PersonTask(Person person){
        this.person = person;
    }
    @Override
    public void run() {
        while (true){
            System.out.println(person);
        }
    }
}
