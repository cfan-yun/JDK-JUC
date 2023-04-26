package com.cyfan.study.a02.locks.aqs.reentraintlock;

import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * 自定义的 aqs
 */
public class MyAbstractQueuedSynchronizer {

    private Thread ownerThread; // 当前持有锁的线程

    private volatile int state; // 线程重入此时计数，线程是否持有锁

    // region  Node节点 操作相关
    static class Node {
        static Node EXCLUSIVE = null; //独占模式
        Node nextWaiter; // 表示共享或者独占模式
        private volatile Thread thread;// 存储的具体对象，Node的元素值
        private volatile Node prev;// 前驱节点
        private volatile Node next;// 后继节点
        private static final int CANCEL = 1;//取消状态
        private static final int INIT = 1;//初始化状态
        private static final int SINGLE = -1;// 可以阻塞的状态
        private volatile int waitState; //节点状态 ，为什么要有这个状态？？？ 因为后驱节点能否被唤醒抢锁，依赖于前驱节点，通过前驱节点的状态来判断

        public Node() {
        }

        public Node(Thread thread, Node mode) {
            this.thread = thread;
            this.nextWaiter = mode;
        }

        public Node getPrev() {
            Node p = prev;
            if (p == null) {
                throw new NullPointerException("");
            } else
                return p;
        }
    }

    private volatile Node head;
    private volatile Node tail;


    protected List<Thread> getQueuedThreads() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (Node t = tail; t != null; t = t.prev) {
            Thread thread = t.thread;
//            if (thread != null) {//头节点的就不取了
                threads.add(thread);

//            }

        }
        return threads;
    }
// endregion


    // region  Unsafe 操作相关
    private static final Unsafe UNSAFE = UnsafeUtils.getUnsafe();
    private static final long stateOffset;
    private static final long tailOffset;
    private static final long headOffset;
    private static final long waitStateOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = UNSAFE.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("state"));
            tailOffset = UNSAFE.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            headOffset = UNSAFE.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("head"));
            waitStateOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("waitState"));
            nextOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("next"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean compareAndSwapState(int expected, int update) {
        return UNSAFE.compareAndSwapInt(this, stateOffset, expected, update);
    }

    private boolean compareAndSwapTail(Node expected, Node update) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, expected, update);
    }

    private boolean compareAndSwapHead(Node expected, Node update) {
        return UNSAFE.compareAndSwapObject(this, headOffset, expected, update);
    }

    private boolean compareAndSwapNext(Node node, Node expected, Node update) {
        return UNSAFE.compareAndSwapObject(node, nextOffset, expected, update);
    }

    public boolean compareAndSwapWaitState(Node node, int expected, int update) {
        return UNSAFE.compareAndSwapInt(node, waitStateOffset, expected, update);
    }


// endregion

    // region 获取锁相关逻辑

    /**
     * 释放锁
     *
     * @param arg 锁重入
     * @return true 释放锁成功
     */
    public boolean release(int arg) {
        //1、state -1
        if (tryRelease(1)) {
            //2.唤醒头结点的后继节点
            Node h = head;
            if (h != null && h.waitState != Node.INIT) {//头节点不为空才需要释放
                unparkSuccessor(h);//唤醒同步队列中被阻塞的线程（重点就是这一句，唤醒的是头结点的后继节点）
                //return true;
            }//头节点为空，队列为空。可以直接释放
            return true;
        }
        return false;
    }

    /**
     * 尝试释放锁
     *
     * @param arg 锁重入次数
     * @return true 释放锁成功
     */
    protected boolean tryRelease(int arg) {
        //这里需要公平锁和非公平锁来继承重写
        throw new UnsupportedOperationException("不支持该方法");
    }

    /**
     * 抢锁
     */
    public void acquire(int arg) {
        //尝试快速获取锁，如果失败了就需要两个操作：1.cas入队列 2.自旋阻塞。
        if (!tryAcquire(arg) &&
                acquireQueue(addNode(Node.EXCLUSIVE), arg)) { // tryAcquire (ture 获取到锁 false 获取锁失败) 在获取锁失败之后，先入队，然后再自旋阻塞.
            //上述判断如果整体返回true的话，说明获取到锁了
            Thread.currentThread().interrupt();// 这里就是补上之前Thread.interrupted() 清除掉的中断标记
        }
    }

    /**
     * 尝试获取锁
     *
     * @return ture 获取到锁 false 获取锁失败
     */
    protected boolean tryAcquire(int arg) {
        //这里需要公平锁和非公平锁来继承重写
        throw new UnsupportedOperationException("不支持该方法");
    }

    /**
     * cas入队列
     *
     * @param mode 模式独占还是共享 Node.
     * @return
     */
    private Node addNode(Node mode) {
        //初始化节点
        Node node = new Node(Thread.currentThread(), mode);// Threadx、y、z
        //System.out.println(node.thread.getName() + ", 创建节点准备排队， tail = " + tail);
        Node prev = tail;// 尾节点赋值给prev变量
        if (prev != null) {//这里是一个优化，快速尝试一次，尝试不成功再去下面casEnQueue中while(true)修改
            //System.out.println(prev);
            node.prev = prev;
            if (compareAndSwapTail(prev, node)) {//cas成功将tail 修改为node，即代表y尾插成功
                prev.next = node;// prev在tail之前，现在
                System.out.println(Thread.currentThread().getName() + " 快速尝试入队完成！");
                return node;
            }
        }
        //1.走到这里表示队列还没有初始化或者tail为空 ，队列不存在 ，if (prev != null) 判断为false
        //2.或者cas失败（if (compareAndSwapTail(prev, node))）。说明队列存在，失败之后需要再次尝试
        casEnQueue(node);//入队完成
        return node;
    }

    private Node casEnQueue(Node node) {
        while (true) {
            Node tmpTail = tail;
            if (tmpTail == null) { // tail 为空，队列都还没有，此时需要初始化
                if (compareAndSwapHead(null, new Node())) { //这里为什么不用线程的Node而是要new一个空节点？？？？？？
                    tail = head;//初始化完成。head,tail都是空节点
                    System.out.println(node.thread.getName() + ", 队列初始化完成!" + tail);
                    //return head;//这里 return new Node()
                }

            } else {//队列不为空，不需要初始化， 其余线程(x,z)在这里自旋入队
                node.prev = tmpTail;
                if (compareAndSwapTail(tmpTail, node)) {//cas成功将tail 修改为node
                    tmpTail.next = node;// prev在tail之前，现在,node称为了新的tail
                    System.out.println(node.thread.getName() + ", 自旋入队完成!");
                    return node;
                }
            }
        }

    }

    /**
     * 自旋抢锁，阻塞
     *
     * @return true 中断标记(发生中断)
     */
    boolean acquireQueue(Node node, int arg) {
        //自旋阻塞
        //1.尝试快速获取锁
        //2.阻塞
        boolean interrupted = false;// 中断标记位；
        boolean occurException = true;  // 是否发生异常
        try {//下面这段逻辑可能发生异常，需要进行处理
            while (true) {
                //1.尝试快速获取锁,前提是检查前驱节点是不是头节点，如果是头结点，才会去快速尝试获取锁
                final Node prev = node.getPrev();//获取到前驱节点(存在NullPointException,但是只要操作队列，prev永远不可能为空，因为有一个空元素的head节点)
                if (prev == head && tryAcquire(arg)) {//如果前驱节点是头结点才去尝试获取锁，如果成功，摘除头结点，自己成为头节点 （tryAcquire 可能抛出方法未实现的异常）
                    setHead(node);//设置自己为头节点
                    prev.next = null; // 设置原头节点的next 为空。
                    occurException = false; // 走到这里，逻辑都走完了，证明没有发生异常 ，
                    return interrupted;
                }
                //2.阻塞，需要检查尝试获取锁失败之后，是否可以被阻塞，shouldParkAfterFailedAcquire
                if (shouldParkAfterFailedAcquire(prev, node) &&
                        parkAndCheckInterrupt()) {//先检查是否可以被挂起
                    interrupted = true; // 只有这个地方是true
                }
            }
        } finally {//try这段代码块中发生异常或者没有异常finaly都会执行，但是cancelAcquire在发生异常时才执行，此时就需要一个标记，标记看是否发生异常
            // 执行取消动作，取消就是把自己从队列中摘除
            if (occurException) {
                cancelAcquire(node);// 这一步必须实现，这里最主要的就是针对 tryAcquire 这个方法抛出的异常来做处理。源码中读写锁/公平锁/非公平锁的tryAcquire是会抛出一个错误的
                //1.这里是否真的跨过了该节点？？？由图分析可知，cancelAcquire没有跨过该节点，只是，next指针跨过了该节点，prev指针并没有跨过。
                //2.prev指针什么时候跨过该节点？？？
                // 2.1是下一个被唤醒的线程执行了shouldParkAfterFailedAcquire方法后才让prev指针跨过该节点的。
                // 2.2下一个被唤醒的线程发生了异常执行cancelAcquire 来做的
            }

        }


    }

    /**
     * 取消获取锁方法
     * 即把当前节点从队列上摘下来
     *
     * @param node 当前节点
     */
    private void cancelAcquire(Node node) {
        if (node == null) {
            return;
        }
        node.thread = null;
        //获取前驱节点
        Node prevNode = node.prev;
        System.out.println(prevNode + "," + node.thread);
        while (prevNode.waitState > 0) {//找到正常的前驱节点
            node.prev = prevNode = prevNode.prev;//从后往前看
        }
        //获取prevNode的后继节点
        Node prevNextNode = prevNode.next;
        //设置当前节点为取消状态
        node.waitState = Node.CANCEL;
        // 分2种情况考虑取消的节点
        // 1.假如该节点是尾节点，
        // 2.假如该节点是中间节点
        if (node == tail && compareAndSwapTail(node, prevNode)) {// 1.该节点是尾节点
            compareAndSwapNext(prevNode, prevNextNode, null);//将前驱节点的next指针修改为空
            //当前节点作为尾节点，持有其他变量的引用是不影响的，因为gc回收对象，只检查当前对象是否被其他对象引用
        } else {//2.该节点是中间节点
            int ws;
            //分两种情况
            //1.前驱节点是头结点
            //2.前驱节点不是头结点
            if (prevNode != head && //1.前驱不是头结点
                    ((ws = prevNode.waitState) == Node.SINGLE || // 前驱状态是 SINGLE 状态 或者 prevNode.waitState 能被修改为 SINGLE状态
                            (ws <= 0 && compareAndSwapWaitState(prevNode, ws, Node.SINGLE))) &&
                    prevNode.thread != null) { // 前驱节点的元素不为空
                Node nodeNext = node.next;//获取当前节点的后继节点
                if (nodeNext != null && nodeNext.waitState <= 0) {//当前节点的后继节点不为空，并且有效
                    compareAndSwapNext(prevNode, prevNextNode, nodeNext);//设置prevNode 的next指针为当前节点的后继节点，即跨过当前节点
                }
            } else {//1.前驱是头结点,
                //直接唤醒当前节点的后继节点(头结点的后继节点才有资格被唤醒)
                //因为这个方法是自旋抢锁、阻塞发生异常之后的取消操作，因此需要完成正常节点应当完成的事情，因为当前节点发生异常了，如果不去唤醒，那么后继节点就没有人去唤醒了
                unparkSuccessor(node);
            }
            node.next = node;//让当前节点的next指向自己，即自己脱离队列
        }
    }

    /**
     * 唤醒后继节点 （通常是头结点来唤醒）
     *
     * @param node 头结点
     */
    private void unparkSuccessor(Node node) {
        int waitState = node.waitState;
        if (waitState < 0) { // 即代表节点是有效状态
            //把该节点恢复为初始状态（因为该节点即将被删除）
            compareAndSwapWaitState(node, waitState, 0);
        }

        Node s = node.next;//获取头结点的后继节点
        if (s == null || s.waitState > 0) {//也就是cancel状态
            s = null; //清空，让下面这个for循环从尾部遍历时，使用;
            for (Node t = tail; t != null && t != node; t = t.prev) {//从队尾往前遍历，因为next指针在变动，可能被修改，导致遍历错误，prev指针没有改变，因而从尾巴遍历
                if (t.waitState <= 0) {// 从尾往头使用prev指针遍历，只要节点状态是有效的，就往前走，直到找到最接近head的节点
                    s = t;
                }
            }
        }

        if (s != null) {//找到唤醒后继节点
            LockSupport.unpark(s.thread);
        }

    }

    /**
     * 2个流程会唤醒park方法
     * 1.正常流程：其他线程执行unpark方法唤醒
     * 2.非正常流程：当前线程执行了中断
     *
     * @return 是否被成功唤醒
     */
    private boolean parkAndCheckInterrupt() {
        LockSupport.park(this); // 被阻塞的线程会卡在这里等待唤醒（unpark 和interrupt 都会 被唤醒）
        //解决中断导致的park失效， isInterrupted 方法不行，这个方法不会清除中断标记位
        boolean interrupted = Thread.interrupted();// 清除中断标记(true -> false)，返回值是是否发生中断，是清除标记之前的标记值(true)
        if (interrupted) {//上面清除的中断标记，一定要在某个地方还原，因为，中断肯定是由业务发起的，清除之后不还原会导致业务中断失败，逻辑错误，破坏业务流程
            System.out.println("发生了中断的，并且interrupted 清除了中断标记");
        }
        return interrupted;
    }

    /**
     * 判断当前节点是否可以被阻塞，根据前驱节点waitState状态判断
     *
     * @param prev 前驱节点
     * @param node 当前节点
     * @return true 可以被阻塞，false 不可以被阻塞
     */
    private boolean shouldParkAfterFailedAcquire(Node prev, Node node) {
        int waitState = prev.waitState;
        if (waitState == Node.SINGLE) { // 判断前驱节点如果是SINGLE 状态 那么前驱节点是可以被阻塞的
            return true; // 只能在这里才能返回true,外层是while(true) 一直在循环修改
        }

        if (waitState > 0) {//取消状态，需要越过取消状态的节点
            do {
                node.prev = prev = prev.getPrev(); // 从后往前看
            } while (prev.waitState > 0);//跨过所有状态为CANCEL的节点
            prev.next = node;

        } else {// waitState =  0 初始化状态
            compareAndSwapWaitState(prev, waitState, Node.SINGLE);//这里只修改，等待外面的死循环再次进来执行上述的 return true
        }
        return false;
    }

    private void setHead(Node node) {
        head = node;// 设置自己为头
        node.thread = null; //头节点永远为空节点
        node.prev = null; // 设置前驱节点为空。
    }

    /**
     * 检查队列中有没有前驱节点，即检查队列中是否存在挂起的线程节点
     *
     * @return false  没有挂起的线程节点 true 有挂起的线程节点
     */
    protected boolean hasQueuePrev() {
//            return false;
        Node h = head;
        Node t = tail;
        Node s;
        return h != t &&   //头节点不等于尾节点,说明队列不为空，中间有节点了
                ((s = h.next) == null || // 头结点之后，没有节点,s 是 头的后继节点
                        s.thread != Thread.currentThread()// 节点中的线程不等于当前线程，说明也没有前驱节点
                );
    }

    // endregion

    //region getter and setter


    public Thread getOwnerThread() {
        return ownerThread;
    }

    public void setOwnerThread(Thread ownerThread) {
        this.ownerThread = ownerThread;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    //endregion

}
