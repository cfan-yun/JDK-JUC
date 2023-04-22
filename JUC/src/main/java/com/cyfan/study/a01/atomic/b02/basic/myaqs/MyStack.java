package com.cyfan.study.a01.atomic.b02.basic.myaqs;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 实现出入栈
 */
public class MyStack {

    private static AtomicReference<Node> stackHead = new AtomicReference<>();

    static class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 入栈操作
     *
     * @param node node节点
     * @return head
     */
    public Node push(Node node) {
        while (true) {
            Node headNode = stackHead.get();
            node.next = headNode;
            synchronized (MyStack.class){//解决打印错乱问题
                if (stackHead.compareAndSet(headNode, node)) {
                    System.out.println(Thread.currentThread().getName() +", stack -> 入栈 ： " + node.value);
                    return headNode;
                }
            }

        }

    }

    /**
     * 出栈
     * @return 栈顶head
     */
    public Node pop(){
        while (true){
            Node headNode = stackHead.get();
            if (headNode == null){
                System.out.println(Thread.currentThread().getName()+ ",栈顶为空！");
                return null;
            }
            Node next = headNode.next;
            synchronized (MyStack.class) {//解决打印错乱问题
                if (stackHead.compareAndSet(headNode, next)) {
                    System.out.println(Thread.currentThread().getName() + ", stack -> 出栈 ： " + headNode.value);
                    headNode.next = null;
                    return headNode;
                }
            }
        }
    }


    /**
     * 打印栈
     */
    public void printStack(){
        Node headNode = stackHead.get();
        while (headNode != null){
            System.out.println(headNode.value);
            headNode = headNode.next;
        }
    }
}
