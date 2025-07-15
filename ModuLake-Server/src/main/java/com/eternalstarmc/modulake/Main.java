package com.eternalstarmc.modulake;

import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.config.ConfigManager;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;
import com.eternalstarmc.modulake.api.plugin.PluginManager;
import com.eternalstarmc.modulake.api.senders.Console;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;
import com.eternalstarmc.modulake.command.CommandManagerImpl;
import com.eternalstarmc.modulake.command.ConsoleImpl;
import com.eternalstarmc.modulake.config.ConfigManagerImpl;
import com.eternalstarmc.modulake.network.ApiRouterManagerImpl;
import com.eternalstarmc.modulake.network.ModuLakeServer;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final ModuLakeServer SERVER;
    public static final ApiRouterManager API_ROUTER_MANAGER = new ApiRouterManagerImpl();
    public static final PluginManager PLUGIN_MANAGER = new PluginManagerImpl();
    public static final CommandManager COMMAND_MANAGER = new CommandManagerImpl();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManagerImpl();
    public static final Console CONSOLE = new ConsoleImpl();
    public static final File PLUGINS_FOLDER = new File("./plugins");
    public static final File DATA_FOLDER = new File("./data");
    public static final File CONFIG_FOLDER = new File("./config");
    public static YamlCore CONFIG;

    static {
        System.out.println("正在启动 " + Main.class.getName());
        new ExitHook();
        CONFIG = ((ConfigManagerImpl) CONFIG_MANAGER).load("config.yml", true);
        SERVER = new ModuLakeServer(CONFIG.getString("listen.host"), CONFIG.getInt("listen.port"));
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (DATA_FOLDER.mkdir()) log.info("正在创建数据文件夹...");
        Thread.currentThread().setName("MAIN-THREAD");
        Init.onFirstInit();
        SERVER.start().isComplete();
        System.out.println("加载完成，耗时 " + (System.currentTimeMillis() - start) + " ms，键入 help 或 ? 获取帮助！");
        ((CommandManagerImpl) COMMAND_MANAGER).startListening();
    }
}