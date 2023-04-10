package com.cyfan.study.a02.mycase.person;

public class MutablePerson {


    private  String name;
    private  String id;


    public MutablePerson(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void  setId(String id) {
        this.id = id;
    }

    public synchronized void setPerson(String id, String name){
        this.id = id;
        this.name  = name;
    }

    @Override
    public String toString() {
        return "MutablePerson{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
