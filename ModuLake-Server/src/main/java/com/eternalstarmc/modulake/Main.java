package com.eternalstarmc.modulake;

import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.plugin.PluginManager;
import com.eternalstarmc.modulake.api.senders.Console;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;
import com.eternalstarmc.modulake.command.CommandManagerImpl;
import com.eternalstarmc.modulake.command.ConsoleImpl;
import com.eternalstarmc.modulake.network.ApiRouterManagerImpl;
import com.eternalstarmc.modulake.network.ModuLakeServer;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {
    public static final ModuLakeServer SERVER;
    public static final ApiRouterManagerImpl API_ROUTER_MANAGER = new ApiRouterManagerImpl();
    public static final PluginManager PLUGIN_MANAGER = new PluginManagerImpl();
    public static final CommandManager COMMAND_MANAGER = new CommandManagerImpl();
    public static final Console CONSOLE = new ConsoleImpl();
    public static final File PLUGINS_FOLDER = new File("./plugins");
    public static final File DATA_FOLDER = new File("./data");
    public static final File CONFIG_FOLDER = new File("./config");
    public static final YamlCore CONFIG = YamlCore.load(new File(CONFIG_FOLDER, "config.json"));
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        System.out.println("正在启动 " + Main.class.getName());
        SERVER = new ModuLakeServer("0.0.0.0", 8080);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (DATA_FOLDER.mkdir()) log.info("正在创建数据文件夹...");
        if (CONFIG_FOLDER.mkdir()) log.info("正在创建配置文件夹...");
        Thread.currentThread().setName("MAIN-THREAD");
        Init.onFirstInit();
        SERVER.start().isComplete();
        System.out.println("加载完成，耗时 " + (System.currentTimeMillis() - start) + " ms，键入 help 或 ? 获取帮助！");
        ((CommandManagerImpl) COMMAND_MANAGER).startListening();
    }
}