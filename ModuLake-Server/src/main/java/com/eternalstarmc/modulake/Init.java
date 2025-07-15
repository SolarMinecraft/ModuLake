package com.eternalstarmc.modulake;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.exception.ExceptionEventBusCodec;
import com.eternalstarmc.modulake.command.CommandManagerImpl;
import com.eternalstarmc.modulake.command.commands.CommandRegister;
import com.eternalstarmc.modulake.config.ConfigManagerImpl;
import com.eternalstarmc.modulake.network.ApiRouterManagerImpl;
import com.eternalstarmc.modulake.network.routers.ServerInfoRouter;
import com.eternalstarmc.modulake.plugin.PluginLoggerManager;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.eternalstarmc.modulake.Main.*;
import static com.eternalstarmc.modulake.api.StaticValues.EVENT_BUS;

public class Init {
    private static final Logger log = LoggerFactory.getLogger(Init.class);
    private static boolean isFirstInit = true;

    protected static void onFirstInit () {
        ModuLake.ModuLakePlugin kernel = ModuLake.getKernel();
        kernel.setPluginManager(PLUGIN_MANAGER);
        kernel.setLogger(PluginLoggerManager.getPluginLogger(kernel));
        kernel.setApiRouterManager(API_ROUTER_MANAGER);
        kernel.setServer(Main.SERVER.getServer());
        ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        kernel.onEnable0();
        ModuLake.setState(true);
        EVENT_BUS.registerCodec(new ExceptionEventBusCodec());
        init();
        isFirstInit = false;
    }

    public static void init () {
        ModuLake.ModuLakePlugin kernel = ModuLake.getKernel();
        if (!kernel.isEnabled()) {
            kernel.onEnable0();
            ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        }
        if (!isFirstInit) {
            try {
                CONFIG.reload();
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    CONFIG = ((ConfigManagerImpl) CONFIG_MANAGER).load("config.yml", true, false);
                }
                log.error("无法重新加载配置文件，请检查配置文件状态！");
            }
        }
        API_ROUTER_MANAGER.registerApiRouter(new ServerInfoRouter());
        PLUGIN_MANAGER.loadPluginsInFolder(PLUGINS_FOLDER);
        CommandRegister.init();
    }

    public static void cleanup () {
        ModuLake.getKernel().onDisable1();
        ((ApiRouterManagerImpl) API_ROUTER_MANAGER).cleanup();
        ((PluginManagerImpl) PLUGIN_MANAGER).cleanup();
        ((CommandManagerImpl) COMMAND_MANAGER).cleanup();
    }
}
