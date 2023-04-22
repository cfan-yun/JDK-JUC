package com.cyfan.study.a01.atomic.b01.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 如何获取Unsafe类
 * 3种方式：
 * 1.将应用打程jar包，放置到bootstrap(启动类加载器)的搜索范围之内，获取Unsafe
 * -Xbootclasspath/a:C:\Users\Lenovo\IdeaProjects\JDK-JUC\out\artifacts\MyUnsafe\MyUnsafe.jar
 * /a表示追加路径,追加bootstrap 类加载器的加载的路径
 * 2.通过构造方法反射获取Unsafe
 * 3.通过字段反射
 */
public class GetUnsafeInstanceTest {

    public static void main(String[] args) throws RuntimeException {

        //1.将应用打程jar包，放置到bootstrap(启动类加载器)的搜索范围之内，获取Unsafe
        //jvm参数 -Xbootclasspath/a:C:\Users\Lenovo\IdeaProjects\JDK-JUC\out\artifacts\MyUnsafe\MyUnsafe.jar
        //-Xbootclasspath/a:C:\Users\Lenovo\IdeaProjects\JDK-JUC\out\artifacts\MyUnsafe\MyUnsafe.jar
//        Unsafe unsafe = Unsafe.getUnsafe();//unsafe的类加载器是Bootstrap，启动类加载器，加载核心jar包，rt.jar等
//        unsafe.getLong(1111);

//        System.out.println("unsafe = " + unsafe);
//        ClassLoader classLoader = MyUnsafe.class.getClassLoader();//当前类的类加载器是sun.misc.Launcher$AppClassLoader@18b4aac2
//        System.out.println("classLoader = " + classLoader);


        //2.通过构造方法反射获取unsafe
        try {
            Constructor<?> constructor = Unsafe.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Unsafe unsafeByConstructor = (Unsafe)constructor.newInstance();
            System.out.println("unsafeByConstructor = " + unsafeByConstructor);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException  e) {
            throw new RuntimeException(e);
        }

        //3.通过字段反射
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Object unsafeByField = (Unsafe)theUnsafe.get(null); //静态变量get中填什么都可以..
            System.out.println("unsafeByField = " + unsafeByField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
