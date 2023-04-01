package com.cyfan.study.a01;


/**
 * 安全门，所有人（线程）通过这个安全门
 */
public class SafeGate {

    private int num;
    private String name;
    private String address;

    public synchronized  void pass(String name, String address){
        this.num++;
        this.name = name;
        this.address = address;
        this.check();
    }

    @Override
    public  String toString() {
        if (this.name.charAt(0) != this.address.charAt(0)){
            System.out.println( "NO."+num +","+this.name + ", "+ this.address);
            return "";
        }
        return "NO."+num +","+this.name + ", "+ this.address;
    }

    private void check() {
        if (this.name.charAt(0) != this.address.charAt(0)){
            System.out.println("################ERRO###############" + this.toString());
        }
    }
}
