package com.cyfan.study.a01.atomic.b04.updater;

/**
 * AtomicReference不能保证引用内部的属性安全
 *
 *  case： 一个古老的项目，有一个实体类， 里面有一个int 类型的属性，现在业务需要多线程修改这个money字段。
 *          Java源文件已经丢失，只有class文件，class文件在jar包中，jar包是通用的不能修改，修改money字段如何保证线程安全？？？
 *          Updater
 *
 */
public class Main {
}
