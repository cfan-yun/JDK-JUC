package com.cyfan.study.a02.mycase.person;

public class MutablePersonTask extends Thread{

    private MutablePerson mutablePerson;

    public MutablePersonTask(MutablePerson mutablePerson){
        this.mutablePerson = mutablePerson;
    }

    @Override
    public void run() {
        while (true){
            ImmutablePerson immutablePerson = new ImmutablePerson(mutablePerson);
            if (!immutablePerson.getName().equals(immutablePerson.getId())){
                System.out.println(Thread.currentThread().getName()+    "##############ERROR"+immutablePerson);
            }
        }
    }
}
