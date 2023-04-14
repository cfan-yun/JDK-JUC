package com.cyfan.study.a10;


/**
 *  Two-Phase Termination模式(两阶段终止模式)
 *      开始-->操作中-->终止处理中-->终止
 *      先从"操作中" 变为 "终止处理中" 状态，然后再真正的终止线程
 *      提供终止线程的一种方式
 *
 *      该模式将停止线程这个动作拆解为两个阶段  准备阶段和执行阶段，从而优雅、安全的停止线程。
 *
 *      case:
 *         一个学生写暑假作业，时间到了，然后妈妈叫他睡觉，那么他先准备(收拾课本、洗漱)、后执行（睡觉）
 *         角色分析：
 *              学生线程：写作业
 *              家长线程：让学生线程睡觉（停止工作）
 *      case2:游戏服务器停止案例
 *          大型游戏系统，3个子系统，公告系统、活动系统、其他系统，主系统发命令重启，子系统所有线程全部停止。
 */
public class Main {
}
