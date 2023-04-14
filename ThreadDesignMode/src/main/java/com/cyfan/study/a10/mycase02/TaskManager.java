package com.cyfan.study.a10.mycase02;

public class TaskManager implements Runnable {

    private final GameSystem gameSystem;

    public TaskManager(GameSystem gameSystem) {
        this.gameSystem = gameSystem;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(gameSystem.getTime());
                if (SystemState.getInstance().isShutdown()){//服务器是否关闭
                    break;
                }
                gameSystem.event();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            gameSystem.event();
        }
    }
}
