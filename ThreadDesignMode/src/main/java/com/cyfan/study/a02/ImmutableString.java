package com.cyfan.study.a02;


public class ImmutableString {

    public static void main(String[] args) {
        String name= new String("abcde");
        String subName = name.substring(1);

        //subName 与name不是同一个对象
        System.out.println("name = " + name);
        System.out.println("subName = " + subName);
        System.out.println("name.hashCode() = " + name.hashCode());
        System.out.println("subName.hashCode() = " + subName.hashCode());

        StringBuilder stringBuilder = new StringBuilder();


    }
}
