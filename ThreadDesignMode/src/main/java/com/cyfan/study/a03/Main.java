package com.cyfan.study.a03;


/**
 * 3.GuardedSuspension 模式
 *      又叫做"保护性暂挂模式"，如果执行现在的处理会造成问题，就让执行处理的线程等待，通过让线程等待来保证实例的安全性。
 *      问题：保护的是谁?暂时挂起的又是谁？（相当于快递暂存柜子，保证快递安全、有空了人再去取）
 *      case:
 *          在tomcat中，client发送request请求，server端接受到请求，然后解析，缓存起来，然后server端线程会获取到request请求，
 *          然后对请求做进一步处理（交给SpringMVC去做了）
 *
 *          服务器前端（依然在服务器这端），专指服务接受请求。
 *          服务器后端，请求转发框架
 *          4个实体：
 *          1）客户端线程，发送request到队列；
 *          2）request
 *          3) queue
 *          5) 服务端线程，负责到队列中获取request做进一步处理
 *
 *          保护queue,暂时挂起的是消费者线程，说白了就是队列中如果没有东西，就挂起消费者线程
 *          核心代码：
 *              while(队列为空){
 *                  synchronized(queue){
 *                      wait();//挂起消费者线程
 *                  }
 *              }
 *
 */
public class Main {
}
