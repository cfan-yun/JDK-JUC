package com.cyfan.study.a09;


/**
 * Future模式
 *  Future是未来的意思。假设有一个方法需要花费很长时间才能获取运行结果。那么与其一直等待结果，不如
 *  先拿一张"提货单"。这里"提货单"就称为Future角色，是未来可以转换为实物的凭证。
 *
 *  跟Thread-Per-Message的差异：
 *      Thread-Per-Message没有返回值
 *      Future模式有返回值
 *
 *  case:
 *      角色分析：
 *          Client线程：
 *          Server:
 *          创建产品的线程-new Thread (这个线程非常重要，这个线程把请求一分为二，从而达到异步的目的)
 *          FutureData(提货单)：未来的数据(公有数据)
 *          Product(RealData)：真实的数据（公有数据）
 *          Test:
 *
 *  使用了哪些设计模式：
 *      ThreadPerMessage
 *      Balking
 *      GuardedSuspension
 *      SingleThreadExecution
 *
 *      FutureTask源码解析
 *      get阻塞
 *      set唤醒
 *      构造函数设置Callable
 *      run时set异步返回数据，从Callable中获取
 *
 *
 *
 *   case03:爬虫（获取网页内容）
 *      需求：采集数据、保存数据、分析数据（爬百度首页）
 *      改造：采集和保存数据用新的线程来做
 *
 *
 */
public class Main {
}
