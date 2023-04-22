package com.cyfan.study.a01.atomic.b01.unsafe.object;

public class Person {

    private int id;

    private Man man;

    private String address;

    private boolean manB;

    private static String defaultString =  "defaultString";


    public int getId() {
        return id;
    }

    public Man getMan() {
        return man;
    }

    public static String getDefaultString() {
        return defaultString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMan(Man man) {
        this.man = man;
    }

    public boolean isManB() {
        return manB;
    }

    public void setManB(boolean manB) {
        this.manB = manB;
    }

    public static void setDefaultString(String defaultString) {
        Person.defaultString = defaultString;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", man=" + man +
                '}';
    }



}
