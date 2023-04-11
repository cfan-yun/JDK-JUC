package com.cyfan.study.a07;

/**
 * Thread-Per-Message 模式 一个命令或请求由一个线程处理
 *  为每个命令或请求分配一个线程，由这个线程来处理。
 *      tomcat早期的BIO模型（bio,apr,nio,aio）
 *    case1:服务提前响应、业务后台执行
 *      角色分析：
 *          Server: 服务器
 *          Handler: 处理请求
 *          Client: 发送请求
 *          Test：测试类
 *
 *      总结：
 *          1）提交了系统响应，缩短了时间
 *          2）对操作的顺序没有要求
 *          3）没有返回值
 *          4）适用于服务器
 *
 *     case2: tomcat bio模型雏形简易实现
 *     需求：刷新浏览器，然后浏览器上不断打印"hello,word！"打印10次，每次间隔1秒
 *     请求：http://localhost:8888/hello
 *     响应：hello,word！
 *
 *
 */
public class Main {
}
