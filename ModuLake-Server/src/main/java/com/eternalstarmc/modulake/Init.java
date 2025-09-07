package com.eternalstarmc.modulake;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.exception.ExceptionEventBusCodec;
import com.eternalstarmc.modulake.command.CommandManagerImpl;
import com.eternalstarmc.modulake.command.commands.CommandRegister;
import com.eternalstarmc.modulake.dependency.InjectManagerImpl;
import com.eternalstarmc.modulake.dependency.creators.DependencyCreatorRegister;
import com.eternalstarmc.modulake.network.ApiRouterManagerImpl;
import com.eternalstarmc.modulake.network.routers.ServerInfoRouter;
import com.eternalstarmc.modulake.placeholder.PlaceHolderManagerImpl;
import com.eternalstarmc.modulake.placeholder.holders.PlaceHolderRegister;
import com.eternalstarmc.modulake.plugin.PluginLoggerManager;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;
import com.eternalstarmc.sql.api.SQLAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import static com.eternalstarmc.modulake.Main.*;
import static com.eternalstarmc.modulake.api.StaticValues.EVENT_BUS;

public class Init {
    private static final Logger log = LoggerFactory.getLogger(Init.class);
    private static boolean isFirstInit = true;

    protected static void onFirstInit () {
        if (CONFIG.getBoolean("database.enable")) {
            switch (CONFIG.getString("database.type").toUpperCase(Locale.ROOT)) {
                case "MYSQL" -> SQLAPI.init(CONFIG.getString("database.host"),
                        CONFIG.getInt("database.port"),
                        CONFIG.getString("database.name"),
                        CONFIG.getString("database.username"),
                        CONFIG.getString("database.password"));
                case "SQLITE" -> SQLAPI.init(CONFIG.getString("database.path"));
                default -> log.warn("配置文件中已启用数据库，但输入的数据库的类型不支持！");
            }
        }
        ModuLake.ModuLakeKernel kernel = ModuLake.getKernel();
        kernel.setPluginManager(PLUGIN_MANAGER);
        kernel.setLogger(PluginLoggerManager.getPluginLogger(kernel));
        kernel.setApiRouterManager(API_ROUTER_MANAGER);
        kernel.setServer(Main.SERVER.getServer());
        ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        kernel.onPluginStartup();
        ModuLake.setState(true);
        EVENT_BUS.registerCodec(new ExceptionEventBusCodec());
        init();
        isFirstInit = false;
    }

    public static void init () {
        ModuLake.ModuLakeKernel kernel = ModuLake.getKernel();
        if (!kernel.isEnabled()) {
            kernel.onPluginStartup();
            ((PluginManagerImpl) PLUGIN_MANAGER).getPluginsMap().put("ModuLake", kernel);
        }
        if (!isFirstInit) {
            try {
                CONFIG.reload();
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    CONFIG = CONFIG_MANAGER.load("config.yml", true, false);
                }
                log.error("无法重新加载配置文件，请检查配置文件状态！");
            }
        }
        API_ROUTER_MANAGER.registerApiRouter(new ServerInfoRouter());
        PLUGIN_MANAGER.loadPluginsInFolder(PLUGINS_FOLDER);
        CommandRegister.init();
        PlaceHolderRegister.init();
        DependencyCreatorRegister.init();
    }

    public static void cleanup () {
        ModuLake.getKernel().onHotReload();
        ((ApiRouterManagerImpl) API_ROUTER_MANAGER).cleanup();
        ((PluginManagerImpl) PLUGIN_MANAGER).cleanup();
        ((CommandManagerImpl) COMMAND_MANAGER).cleanup();
        ((PlaceHolderManagerImpl) PLACE_HOLDER_MANAGER).cleanup();
        ((InjectManagerImpl) INJECT_MANAGER).cleanup();
    }
}
