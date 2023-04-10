package com.cyfan.study.a02.mycase.line;

public class LineTest {

    public static void main(String[] args) {
        Pointer start = new Pointer(0, 0);
        Pointer end = new Pointer(0, 5);

        Line line = new Line(start, end);
        System.out.println("line = " + line);


    }
}


