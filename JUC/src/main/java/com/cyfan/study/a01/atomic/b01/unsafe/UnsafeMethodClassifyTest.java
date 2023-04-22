package com.cyfan.study.a01.atomic.b01.unsafe;

import com.cyfan.study.a01.atomic.b01.unsafe.object.User;
import com.cyfan.study.a01.atomic.b01.unsafe.object.CasObject;
import com.cyfan.study.a01.atomic.b01.unsafe.object.Man;
import com.cyfan.study.a01.atomic.b01.unsafe.object.Person;
import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * unsafe方法分类
 * 1.对内存操作（堆外内存）
 *      源码直接调用操作系统函数allocateMemery
 * 2.操作数组
 *
 * 3.操作对象
 *  重点：putObjectVolatile
 *        putOrderedObject
 *  c++层面上这两个方法的源码一摸一样，但是jit会帮我们在汇编层面上在 putObjectVolatile上面加SoreLoad屏障（lock addl）
 *  怎么实现的？？？
 *  方法内联：
 *      vmSymbols.hpp -> (搜索)putObjectVolatile -> do_intrinsic(_putObjectVolatile...)这里面_putObjectVolatile是一个ID会根据不同的ID 生成不同的指令集，差异就在这里
 *      ->library_call.cpp ->
 *      case vmIntrinsics::_putObjectVolatile:        return inline_unsafe_access(!is_native_ptr,  is_store, T_OBJECT,   is_volatile);
 *      case vmIntrinsics::_putOrderedObject:         return inline_unsafe_ordered_store(T_OBJECT);
 *      -> inline_unsafe_access  -> is_volatile -> insert_mem_bar -> MemBarVolatile -> x86_64.ad -> $$emit$$"lock addl [rsp + #0], 0\t! membar_volatile"
 * 4.线程调度
 *      monitorEnter/monitorExit/tryMonitorEnter
 *      park/unpark  阻塞/唤醒
 * 5.操作Class
 *      staticFieldOffset/staticFieldBase/shouldBeInitialized
 *      defineClass/defineAnonymousClass
 * 6.操作内存屏障
 *      loadFence/storeFence/fullFence
 * 7.获取系统信息
 *   pageSize 获取内存页大小 2的整数幂
 *   addressSize 返回系统指针大小 64位-8 32位-4
 * 8.对cas操作（compareAndSwap 比较并交换）
 *   compareAndSwapObject/compareAndSwapInt/compareAndSwapLong
 *   底层: cmpxchgq 指令
 *   Atomic::cmpxchg(x, addr, e) //关键代码 cas操作，将地址addr的值修改为x , x 的原始预期值为e(addr中的原值)， 如果预期值是e，修改为x
 *      gcc： cmpxchgq %1, (%3) exchange_value(x) compare_value(e在RAX寄存器里)  dest(addr变量地址)
 *      intel:  cmpxchgq %1, (%3) exchange_value(x)  dest(addr变量地址) compare_value(e在RAX寄存器里)
 *          cmpxchgq 4, dest, rax(3)
 *          1.dest(3), 如果Rax寄存器中的值，与dest地址中值相等，那么将exchange_value 填入 dest内存地址中（dest(4), 返回rax(3)）
 *          2.dest(2), 如果不相等，则把desc地址中所存储的值存入rax寄存器（dest(2) 返回rax(2)）
 *          3.最后将rax中的值放到exchange_value 中返回
 *      ···实现：mycmpchg.c
 *
 *          cmpxchgq 4, dest, 3
 *                       3      compare_value = 3 被加载到rax(寄存器中)  比较rax(3) 与 dest(3) 相等，修改dest(3) -> dest(4) return rax(3)
 *                       2      compare_value = 3 被加载到rax(寄存器中)  比较rax(3) 与 dest(2) 不相等，修改rax(3) -> rax(2) return rax(2)
 *          总结：无论是否相等都是返回dest 没被修改的值的值，即最开始进入方法时，dest的原值。
 *          伪代码：
 *              rax =  compare_value;
 *              if(rax ==  dest){
 *                  zf=1;
 *                  dest =  exchange_value;
 *              }else{
 *                  zf = 0;
 *                  rax =  dest;
 *              }
 *            exchange_value =  rax;
 *            return exchange_value;
 */
/*
 * 查看volatile汇编指令层面查看
 *
 * 需要几个JVM参数
 *
 * --HSDIS工具使用
 * hotspot 使用内嵌jit编译，jit又分c1编译器和c2编译器
 *       c1--client
 *       c2--server  尽最大的可能进行编译优化
 *  冒号后面“+”代表启用，“-”代表禁用
 * -server                              //使用c2编译器优化编译
 * -XX:+UnlockDiagnosticVMOptions       //解锁诊断功能，打印汇编代码，会认为你需要做诊断
 * -XX:+PrintAssembly                   //打印汇编代码
 * -XX:-Inline                          //内联
 * -XX:-TieredCompilation               //只开启c2,禁用中间编译层(123层)
 * -XX:+TieredCompilation  -XX:TieredStopAtLevel=1  //只开启c1
 *
 *
 */

public class UnsafeMethodClassifyTest {

    private static final AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {

        Unsafe unsafe = UnsafeUtils.getUnsafe();
        //1.unsafe 操作内存
//        memorySomething(unsafe);
        //2.unsafe 操作数组
//        arraySomething(unsafe);
        //3.unsafe 操作对象
//        objectSomething(unsafe);
        //3.1 这两个方法在c++层面上源码一样，那么汇编层面上有什么区别？？
//        testOrderedObject2ObjectVolatile();//atomic
//        testOrderedObject2ObjectVolatile(unsafe);//undafe
        //4.unsafe 操作线程
//        threadSomething(unsafe);
        //4.1 unsafe 阻塞/唤醒线程
//        threadParkSomething(unsafe);
//        threadParkUnParkSomething(unsafe);
        //5.unsafe 操作Class
//        classSomething(unsafe);
        //6.unsafe 操作内存屏障
//        memoryBarrierSomething(unsafe);
        //7.unsafe 获取系统参数
//        systemParamSomething(unsafe);
        //8.unsafe 对cas操作
        casSomething(unsafe);
    }

    private static void casSomething(Unsafe unsafe) {
//        unsafe.compareAndSwapInt();
//        unsafe.compareAndSwapLong();
//        unsafe.compareAndSwapObject()
        CasObject casObject = new CasObject();
        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + ", number =" +casObject.increment(i, unsafe));
            }
        }, "t1").start();

        new Thread(()->{
            for (int i = 11; i <= 20; i++) {
                System.out.println(Thread.currentThread().getName() + ", number =" +casObject.increment(i, unsafe));
            }
        }, "t2").start();

    }

    private static void memoryBarrierSomething(Unsafe unsafe) {
        //unsafe.loadFence();//读屏障(loadload) 确保屏障之前的读全部完成（invalidate queue 全部置无效） x86上其实没这个屏障 x86没有invalidate queue load1 | loadload | load2 确保load2 一定在load1之后执行
        //unsafe.storeFence();//写屏障(storestroe) 确保屏障之前的写操作全部完成（store buffer 中的变动全部Flush cache） x86上其实没这个屏障，x86中写肯定进store buffer 排队 store1 |storestroe|  store2 确保store2 一定在store1 之后执行
        //unsafe.fullFence();//全屏障(stroeload) x86下只有这个屏障 store1 |stroeload|  store2/load1 确保 store2/load1 一定在store1之后执行
    }

    public static void memorySomething(Unsafe unsafe) throws InterruptedException {
        long address = 0L;
        long address1 = 0L;
        try {
            address = unsafe.allocateMemory(16);//开辟分配内存最好是8的倍数
            //Thread.sleep(100000);//休眠之后使用 Java VisualVM工具观看，监控发现调大分配的内存，gc总的几个区域无任何变化
            System.out.println("分配的16个字节的堆外内存地址为：" + address);

            unsafe.putLong(address, 123456);//内存赋值
            long aLong = unsafe.getLong(address);//获取内存值

            System.out.println("aLong = " + aLong);

            address1 = unsafe.allocateMemory(16);//开辟分配内存最好是8的倍数
            System.out.println("address1 = " + address1);
            long aLong1 = unsafe.getLong(address1);//获取内存值

            System.out.println("aLong1 = " + aLong1);

            unsafe.copyMemory(address, address1, 16);//拷贝内存值

            aLong1 = unsafe.getLong(address1);//获取内存值
            System.out.println("aLong1 = " + aLong1);

            unsafe.reallocateMemory(address1, 32);//重分配内存
            unsafe.setMemory(address1, 32, (byte) 1);//设置内存填充，表示32个字节都填充 每个字节都填充00000001
            System.out.println("address1内存扩容之后值：" + unsafe.getByte(address1)); //1个字节
            System.out.println("address1内存扩容之后值：" + unsafe.getInt(address1)); //4个字节 00000001000000010000000100000001

            //设置数组值
            int[] b = {1, 2, 3, 4};
            int baseOffset = unsafe.arrayBaseOffset(int[].class);
            //修改地址, 偏移地址，修改偏移位置后多少个字节，修改值用来填充单个字节的数值
            unsafe.setMemory(b, baseOffset + 8, 8, (byte) 6); //00000011000000110000001100000011
            System.out.println(Arrays.toString(b));

            //数组内容拷贝 a 拷贝到a1
            int[] a = {1, 2, 3, 4};
            int[] a1 = new int[4];
            int baseOffset1 = unsafe.arrayBaseOffset(int[].class);
            // 来源数组，来源数组指针，目标数组，目标数组指针，需要拷贝多少个字节
            unsafe.copyMemory(a, baseOffset1, a1, baseOffset1, 16);
            System.out.println("a1 = " + Arrays.toString(a1));


        } finally {
            unsafe.freeMemory(address); //释放内存
            unsafe.freeMemory(address1); //释放内存
        }
    }


    public static void arraySomething(Unsafe unsafe) {

        String[] strings = {"111", "222", "abfjash"};
        int baseOffset = unsafe.arrayBaseOffset(String[].class);//基地址
        int scale = unsafe.arrayIndexScale(String[].class);//元素之间的间隔
        System.out.println("baseOffset = " + baseOffset);
        System.out.println("scale = " + scale);

        long[] longs = {1, 2, 3, 4};
        int baseOffsetLong = unsafe.arrayBaseOffset(long[].class);//基地址
        int scaleLong = unsafe.arrayIndexScale(long[].class);//元素之间的间隔
        System.out.println("baseOffsetLong = " + baseOffsetLong);
        System.out.println("scaleLong = " + scaleLong);

        //获取数组中值
        for (int i = 0; i < strings.length; i++) {
            long offset = baseOffset + (long) scale * i;
            Object object = unsafe.getObject(strings, offset);//获取对象值
            System.out.println("strings[" + i + "] = " + object);
        }


    }


    private static void objectSomething(Unsafe unsafe) {

        try {
            //1.创建对象
            Man man = (Man) unsafe.allocateInstance(Man.class);
            System.out.println("man = " + man);

            //2.获取对象值
            Person person = new Person();
            person.setId(1);
            person.setMan(new Man());
            //获取非静态对象字段(基础类型)在对象中的偏移量
            long offsetId = unsafe.objectFieldOffset(Person.class.getDeclaredField("id"));
            //获取静态对象字段(非基础类型Object)在对象中的偏移量
            long offsetDefaultString = unsafe.staticFieldOffset(Person.class.getDeclaredField("defaultString"));
            //获取非静态对象字段(非基础类型Object)在对象中的偏移量
            long offsetMan = unsafe.objectFieldOffset(Person.class.getDeclaredField("man"));
            //获取id字段值
            int id = unsafe.getInt(person, offsetId);
            //获取offsetDefaultString静态字段对象，注意静态变量一定是用Class字节码对象
            String defaultString = (String) unsafe.getObject(Person.class, offsetDefaultString);
            //获取man字段值
            Man manPerson = (Man) unsafe.getObject(person, offsetMan);

            System.out.println("id = " + id);
            System.out.println("defaultString = " + defaultString);
            System.out.println("manPerson = " + manPerson);

            //对象赋值
            Person unsafePerson = (Person) unsafe.allocateInstance(Person.class);

            unsafe.putInt(unsafePerson, offsetId, 10);
            unsafe.putObject(Person.class, offsetDefaultString, "update String");
            unsafe.putObject(unsafePerson, offsetMan, man);
            System.out.println("updateDefaultString = " + Person.getDefaultString());

            System.out.println("unsafePerson = " + unsafePerson);
        } catch (InstantiationException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * jvm参数
     * -server
     * -XX:+UnlockDiagnosticVMOptions
     * -XX:+PrintAssembly
     * -XX:-Inline
     */
    private static void testOrderedObject2ObjectVolatile() {

        /**
         * putObjectVolatile
         *
         *          store1
         *      ----storeLoad----
         *          store2/load
         *
         *     store1是写操作，加了 storeLoad 屏障之后，确保 store2/load 一定是在 store1 写完并且刷到高速缓存Cache之后
         */
//        for (int i = 0; i < 10000; i++) {
//            atomicLong.set(i);//本质上调用 putObjectVolatile 底层会有 lock 前缀指令
//            // 0x00000188eeac7dce: lock addl $0x0,(%rsp)     ;*putfield value  ***lock addl(lock指令前缀)
//        }

        /**
         * putOrderedObject
         *
         *          store1
         *      ----storeStore---
         *          store2
         *
         *     store1是写操作，加了 storeStore 屏障之后，确保 store2一定是在 store1 写完并且刷到高速缓存Cache之后
         *
         *     这就是putObjectVolatile和putOrderedObject的区别
         */
        for (int i = 0; i < 10000; i++) {
            atomicLong.lazySet(i);//本质上调用 putOrderedObject 底层没有屏障x86下没有这个屏障
        }


    }


    /**
     * jvm参数
     * -server
     * -XX:+UnlockDiagnosticVMOptions
     * -XX:+PrintAssembly
     * -XX:-Inline
     *
     * @param unsafe unsafe对象
     */
    private static void testOrderedObject2ObjectVolatile(Unsafe unsafe) {

        try {
            //获取非静态对象字段(基础类型)在对象中的偏移量
            long offsetId = unsafe.objectFieldOffset(Person.class.getDeclaredField("id"));
            //获取静态对象字段(非基础类型Object)在对象中的偏移量
            long offsetDefaultString = unsafe.staticFieldOffset(Person.class.getDeclaredField("defaultString"));
            //获取非静态对象字段(非基础类型Object)在对象中的偏移量
            long offsetMan = unsafe.objectFieldOffset(Person.class.getDeclaredField("man"));
            long offsetAddress = unsafe.objectFieldOffset(Person.class.getDeclaredField("address"));
            long offsetManB = unsafe.objectFieldOffset(Person.class.getDeclaredField("manB"));

            Person person = new Person();
            person.setId(1);
            person.setMan(new Man("zhangsan", "20"));
            person.setAddress("address");
            person.setManB(true);//使用putObjectVolatile 操作赋值 相当于volatile写，所以不用加上volatile

            //测试程序正确性
//            unsafe.putOrderedObject(person, offsetAddress, "addressOrder");
//            String address = (String)unsafe.getObject(person, offsetAddress);
//            System.out.println("address = " + address);

            //测试putOrderedObject
            for (int i = 0; i < 10000000; i++) {
                unsafe.putOrderedObject(person, offsetAddress, "addressOrder");
            }

            //测测试putObject
//            for (int i = 0; i < 10000000; i++) {
//                unsafe.putObject(person, offsetAddress, "addressNoOrder");
//            }

            //测试putObjectVolatile 只有这个会加 lock前缀（即volatile）
//            for (int i = 0; i < 1000000000; i++) {//如果出不来lock指令，那么加多循环次数
//                unsafe.putObjectVolatile(person, offsetManB, false); //lock addl $0x0,(%rsp)  ;*invokevirtual putObjectVolatile
//            }


        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    private static void threadSomething(Unsafe unsafe) {
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println(name + ",正在运行。。。。。");
                unsafe.monitorEnter(lock);//获取锁
                System.out.println(name + ",获取到锁了。。。。。");
                Thread.sleep(1000);
                System.out.println(name + ",执行业务逻辑完成");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                unsafe.monitorExit(lock);//锁退出
                System.out.println(Thread.currentThread().getName() + "锁退出了");

            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println(name + ",正在运行。。。。。");
                unsafe.monitorEnter(lock);//获取锁
                System.out.println(name + ",获取到锁了。。。。。");
                Thread.sleep(1000);
                System.out.println(name + ",执行业务逻辑完成");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                unsafe.monitorExit(lock);//锁退出
                System.out.println(Thread.currentThread().getName() + "锁退出了");

            }
        }, "t2");
        Thread t3 = new Thread(() -> {
            boolean b = false;
            try {
                String name = Thread.currentThread().getName();
                System.out.println(name + ",正在运行。。。。。");
                b = unsafe.tryMonitorEnter(lock);//获取锁,获取到的话就安全执行，获取不到，那就不安全执行了
                System.out.println(name + ",尝试获取锁结果。。。。。" + b);
                Thread.sleep(1000);
                System.out.println(name + ",执行业务逻辑完成");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (b) {//获取到锁了那么退出锁
                    unsafe.monitorExit(lock);//锁退出
                    System.out.println(Thread.currentThread().getName() + "锁退出了");
                }


            }
        }, "t3");


        t1.start();
        t2.start();
        t3.start();


    }

    private static void threadParkSomething(Unsafe unsafe) {
        //unsafe.park(isAbsolute,thread);//isAbsolute是否绝对时间 true time是微秒(休眠6秒，time = 当前系统时间+6秒),false time是纳秒(休眠6秒, time =  6秒)
        //unsafe.unpark(thread);//唤醒哪个线程

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss");
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + Thread.currentThread().getName() + "运行开始。。");

        //阻塞当前线程5秒
        unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + Thread.currentThread().getName() + "阻塞5秒完成。。继续。。");
        //继续阻塞三秒
        unsafe.park(false, TimeUnit.SECONDS.toNanos(3));
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + Thread.currentThread().getName() + "阻塞3秒完成。。继续 false , 0");
        unsafe.park(false, 0);//这里会阻塞，阻塞后需要unpark才能被唤醒
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + Thread.currentThread().getName() + "阻塞0秒会被挂起，唤醒后才执行。。");

    }

    private static void threadParkUnParkSomething(Unsafe unsafe) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss");
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 正在运行。。。");
            unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(600));//休眠10分钟
            System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 继续。。。");
        }, "t1");
        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 正在运行。。。");
            unsafe.park(true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(600));//休眠10分钟
            System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 继续。。。");
        }, "t2");


        t1.start();
        t2.start();

        try {
            Thread.sleep(3000); //休眠3秒
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 开始唤醒线程。。。");
        unsafe.unpark(t1);
        unsafe.unpark(t2);
        System.out.println(Thread.currentThread().getName() + ", " + LocalDateTime.now().format(dateTimeFormatter) + " 唤醒线程完成。。。");


    }

    private static void classSomething(Unsafe unsafe) {
        try {
            Field nameField = User.class.getDeclaredField("name");
            boolean isShouldInit = unsafe.shouldBeInitialized(User.class);//查看对象是否应该被初始化 ，ture 表示还应该被初始化
            long nameOffset = unsafe.staticFieldOffset(nameField);//获取字段在Class中定义的字段偏移量
            Object nameBase = unsafe.staticFieldBase(nameField);//获取到字段的所属类的实际对象类型（全类名Class对象）
            Object nameObject = unsafe.getObject(nameBase, nameOffset);//获取静态变量值，通过 Object.class + 字段偏移量
            System.out.println("isShouldInit = " + isShouldInit);
            System.out.println("nameOffset = " + nameOffset);
            System.out.println("nameBase = " + nameBase);
            System.out.println("nameObject = " + nameObject);

            new User();//这一句没有任何地方用，目的是为了让jvm加载字节码到内存，那么static属性才会被初始化
            boolean isShouldInitByInitAfter = unsafe.shouldBeInitialized(User.class);//查看对象是否应该被初始化 ，ture 表示还应该被初始化
            Object nameObjectByInitAfter = unsafe.getObject(nameBase, nameOffset);//获取静态变量值，通过 Object.class + 字段偏移量
            System.out.println("isShouldInitByInitAfter = " + isShouldInitByInitAfter);
            System.out.println("nameObjectByInitAfter = " + nameObjectByInitAfter);

            //动态定义Class
            String fileName = "C:\\Users\\Lenovo\\IdeaProjects\\JDK-JUC\\JUC\\target\\classes\\com\\cyfan\\study\\a01\\atomic\\unsafe\\object\\User.class";
            File file = new File(fileName);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] content = new byte[(int) file.length()];//定义数组保存
                fileInputStream.read(content);//文件内容读到content中保存
                //动态定义字节码对象
                // name 字节码的文件名   b 字节码文件的二进制数据  off 起始位置 len 字节数组长度  loader 类加载器（null 默认为当前调用类的） protectionDomain 保护域
                Class<?> clazz = unsafe.defineClass(null, content, 0, content.length, null, null);
                //反射操作
                Object defineClassUser = clazz.newInstance();

                clazz.getMethod("setID", String.class).invoke(defineClassUser, "111");
                Object getID = clazz.getMethod("getID").invoke(defineClassUser, null);
                System.out.println("defineClassUser = " + defineClassUser);
                System.out.println("getID = " + getID);

                //动态定义匿名类 Java17不提供
                Class<?> anonymousClass = unsafe.defineAnonymousClass(User.class, content, null);
                System.out.println("anonymousClass = " + anonymousClass);


            }

        } catch (NoSuchFieldException | IOException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private static void systemParamSomething(Unsafe unsafe) {
        int pageSize = unsafe.pageSize(); //获取内存页大小 单位字节
        int addressSize = unsafe.addressSize();// 获取系统指针大小 单位字节，即每个地址的是多少个字节， 64位-8字节 32位-4字节

        System.out.println("pageSize = " + pageSize);
        System.out.println("addressSize = " + addressSize);
    }

}
