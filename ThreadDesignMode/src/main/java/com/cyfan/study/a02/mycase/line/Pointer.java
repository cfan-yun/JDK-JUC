package com.cyfan.study.a02.mycase.line;

/**
 * 点
 */
public class Pointer {

    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Pointer(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Pointer{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
