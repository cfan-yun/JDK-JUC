package com.cyfan.study.a05.mycase;

public class Product {
    private long id;
    private String value;

    public Product(long id) {
        this.id = id;
        this.value = "Product NO."+id;
    }


    @Override
    public String toString() {
        return this.value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
