package com.cyfan.my.test.thread.safe;

import java.security.*;

public abstract class SafeRunnable implements Runnable {

    public  abstract void protectMethod();//该方法提供给第三方实现，该方法中可能会有破坏性代码，删除系统文件，强制关闭虚拟机等

    @Override
    public void run() {

        //安全管理器，当前线程的run方法中有效
        SecurityManager securityManager = new SecurityManager();
        System.setSecurityManager(securityManager);

        //代码来源
        CodeSource codeSource = new CodeSource(null, (CodeSigner[]) null);
        Permissions permissions = new Permissions();//权限
        ProtectionDomain protectionDomain = new ProtectionDomain(codeSource, permissions);//代码来源和权限组成保护域

        AccessControlContext accessControlContext = new AccessControlContext(new ProtectionDomain[]{protectionDomain});//访问控制域

        //对于需要受保护的代码，放到保护域中进行保护。
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            protectMethod(); //第三方实现
            return null;
        },accessControlContext); //（受保护的代码，访问控制域）


    }
}
