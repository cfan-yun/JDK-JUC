package com.cyfan.study.a02.mycase.person;

public class ImmutablePerson {

    private final String name;
    private final String id;


    public ImmutablePerson(String name, String id) {
        this.name = name;
        this.id = id;
    }


    public ImmutablePerson(MutablePerson mutablePerson) {
        //synchronized (mutablePerson){//这个锁和MutablePerson中的锁是同一把锁，没有这个锁会导致
            this.name = mutablePerson.getName();
            this.id = mutablePerson.getId();
        //}

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ImmutablePerson{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
