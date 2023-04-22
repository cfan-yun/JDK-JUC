package com.cyfan.study.a01.atomic.b02.basic;

public class MemberShip {
    private String id;
    private String name;
    private int point;

    public MemberShip(String id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "MemberShip{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", point=" + point +
                '}';
    }
}
