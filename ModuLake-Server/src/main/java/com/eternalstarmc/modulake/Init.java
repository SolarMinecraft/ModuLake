package com.eternalstarmc.modulake;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.exception.ExceptionEventBusCodec;
import com.eternalstarmc.modulake.command.CommandManagerImpl;
import com.eternalstarmc.modulake.command.commands.CommandRegister;
import com.eternalstarmc.modulake.network.routers.ServerInfoRouter;
import com.eternalstarmc.modulake.plugin.PluginLoggerManager;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;

import static com.eternalstarmc.modulake.Main.*;
import static com.eternalstarmc.modulake.api.StaticValues.EVENT_BUS;

public class Init {
    protected static void onFirstInit () {
        ModuLake.ModuLakePlugin kernel = ModuLake.getKernel();
        kernel.setPluginManager(PLUGIN_MANAGER);
        kernel.setLogger(PluginLoggerManager.getPluginLogger(kernel));
        kernel.setApiRouterManager(API_ROUTER_MANAGER);
        kernel.setServer(Main.SERVER.getServer());
        ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        kernel.onEnable0();
        ModuLake.setState(true);
        new ExitHook();
        EVENT_BUS.registerCodec(new ExceptionEventBusCodec());
        init();
    }

    public static void init () {
        ModuLake.ModuLakePlugin kernel = ModuLake.getKernel();
        if (!kernel.isEnabled()) {
            kernel.onEnable0();
            ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        }
        API_ROUTER_MANAGER.registerApiRouter(new ServerInfoRouter());
        PLUGIN_MANAGER.loadPluginsInFolder(PLUGINS_FOLDER);
        CommandRegister.init();
    }

    public static void cleanup () {
        ModuLake.getKernel().onDisable1();
        API_ROUTER_MANAGER.cleanup();
        ((PluginManagerImpl) PLUGIN_MANAGER).cleanup();
        ((CommandManagerImpl) COMMAND_MANAGER).cleanup();
    }
}
