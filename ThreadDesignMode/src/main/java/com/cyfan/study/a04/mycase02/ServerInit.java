package com.cyfan.study.a04.mycase02;

/**
 * 服务器初始化模拟
 *
 */
public class ServerInit {

    private boolean initialized =  false;

    public void init(){
        synchronized (this){
            if (initialized){//判断守护条件，该方法多线程访问
                return;
            }
            startInit();
            initialized = true;
        }

    }

    private void startInit() {
        System.out.println("===============服务器开始初始化===================");
    }
}
