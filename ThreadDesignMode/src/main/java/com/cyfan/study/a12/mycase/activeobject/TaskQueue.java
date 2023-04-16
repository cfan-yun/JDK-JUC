package com.cyfan.study.a12.mycase.activeobject;

/**
 * 自定义数组实现队列
 */
public class TaskQueue<T> {

    private static final int DEFAULT_CAPACITY = 100;

    private final Object[] buffer;

    private int tail; //生产者只关注tail
    private int head; //消费者只关注head
    private final int capacity; //容量
    private int count;//现有元素个数



    public TaskQueue() {
        this.buffer = new Object[DEFAULT_CAPACITY];
        this.tail = 0;
        this.head = 0;
        this.count = 0;
        this.capacity = DEFAULT_CAPACITY;
    }

    public TaskQueue(int capacity) {
        this.buffer = new Object[capacity];
        this.tail = 0;
        this.head = 0;
        this.count = 0;
        this.capacity = capacity;
    }


    /**
     * 生产者调用
     *
     * @param e 元素
     * @throws InterruptedException 中断异常
     */
    public void put(T e) throws InterruptedException {
        synchronized (this) {
            //System.out.println(Thread.currentThread().getName() + ", put " + e.toString());
            //什么时候不能生产？？
            //现有产品数量>=队列容量
            while (count >= buffer.length) {
                //System.out.println(Thread.currentThread().getName()+"put时，满");
                this.wait();
            }
            buffer[tail] = e;
//            tail++;
//            if (tail >= buffer.length){
//                tail = 0;
//            }
            //上述简便写法
            tail = (tail + 1) % buffer.length;//capacity = 10
            count++;
            this.notifyAll();//唤醒等待的线程（生产者和消费者线程）
        }

    }


    /**
     * 消费者调用
     *
     * @return e
     * @throws InterruptedException 中断异常
     */
    public T take() throws InterruptedException {
        synchronized (this) {
            //什么时候不能消费？？？
            // 队列中没有元素
            while (count <= 0) {
                //System.out.println(Thread.currentThread().getName() + "take 时，空");
                wait();
            }
            T o = (T) buffer[head];
            head = (head + 1) % buffer.length;//capacity = 10
            count--;
            notifyAll();//通知生产者和消费者，只要wait卡住的线程都会被唤醒
            //System.out.println(Thread.currentThread().getName() + ", take " + o.toString());
            return o;
        }
    }


    public int getCapacity() {
        return capacity;
    }
}

