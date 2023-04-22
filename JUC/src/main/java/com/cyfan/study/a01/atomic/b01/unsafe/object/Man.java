package com.cyfan.study.a01.atomic.b01.unsafe.object;

public class Man {
    String name;
    String age;

    static {
        System.out.println("Static Block");
    }

    public Man(){
        System.out.println("Constructor Method no args ");
    }

    public Man(String name, String age) {
        this.name = name;
        this.age = age;

        System.out.println("Constructor Method has args ");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Man{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
