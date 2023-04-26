package com.cyfan.study.other;

import javax.xml.soap.Node;

public class ReferenceTest {

    public static void main(String[] args) {

        ReferenceTest referenceTest = new ReferenceTest();
        referenceTest.test();

    }


    public  void test(){
        Node t =  head;
        System.out.println(t);
        System.out.println(head);
        head = new Node(2);
        System.out.println(t);
        System.out.println(head);
    }


    private Node head = new Node(1);
    static class Node {
        private Node next;
        private int state;
        private Node prev;

        public Node(int state) {
            this.state = state;
        }
    }
}
