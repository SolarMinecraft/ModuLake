package com.eternalstarmc.modulake;

public class ExitHook {
    public ExitHook () {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭ModuLake...");
            System.out.println("正在卸载所有插件");
            Init.cleanup();
            Main.SERVER.stop();
            System.out.println("服务器主要部分卸载完成");
        }));
    }
}
