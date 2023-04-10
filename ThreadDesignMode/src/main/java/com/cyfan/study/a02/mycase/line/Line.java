package com.cyfan.study.a02.mycase.line;

/**
 * 线段
 */
public class Line {

    private final Pointer start; //此处需保证，pointer对象中的属性不可被修改，1.public 属性，2.set方法
    private final Pointer end; //此处需保证，pointer对象中的属性不可被修改，1.public 属性，2.set方法

    public Line(Pointer start, Pointer end) {
        //不安全的写法
        //this.start = start;//这种写法可能导致Pointer对象在外面通过引用调用set方法被修改
        //this.end = end;//这种写法可能导致Pointer对象在外面通过引用调用set方法被修改
        //稳妥的写法
        this.start = new Pointer(start.getStart(), start.getEnd());
        this.end = new Pointer(end.getStart(), end.getEnd());

    }

    public Line(int startX, int startY, int endX, int endY) {
        this.start = new Pointer(startX, startY);
        this.end = new Pointer(endX, endY);
    }

    public int getStartX(){
        return  this.start.getStart();
    }
    public int getStartY(){
        return  this.start.getEnd();
    }
    public int getEndX(){
        return  this.end.getStart();
    }
    public int getEndY(){
        return  this.end.getEnd();
    }


    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
