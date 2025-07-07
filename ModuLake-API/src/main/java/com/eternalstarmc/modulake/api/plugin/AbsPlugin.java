package com.eternalstarmc.modulake.api.plugin;

import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;
import com.eternalstarmc.modulake.api.network.Server;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.file.*;

public abstract class AbsPlugin implements PluginBase {
    private Logger logger;
    private File dataFolder;
    private ApiRouterManager apiRouterManager;
    private Server server;
    private PluginManager pluginManager;
    private PluginDescription description;
    private File sourceFile;
    private boolean isEnabled = false;
    private PluginClassLoader classLoader;
    private CommandManager commandManager;

    public void onEnable0 () {
        logger.info("正在启动插件 {} {}", description.name(), description.version());
        onEnable();
        this.isEnabled = true;
    }

    public void onDisable0 () {
        logger.info("正在卸载插件 {} {}", description.name(), description.version());
        onDisable();
        this.isEnabled = false;
        cleanup();
    }

    public void onDisable1 () {
        logger.info("正在卸载插件 {} {}", description.name(), description.version());
        onDisable();
        this.isEnabled = false;
    }

    public abstract void onEnable ();

    public abstract void onDisable ();

    @Override
    public Logger getLogger() {
        return logger;
    }

    public void setLogger (Logger logger) {
        this.logger = logger;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder (File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public ApiRouterManager getApiRouterManager() {
        return apiRouterManager;
    }

    public void setApiRouterManager (ApiRouterManager apiRouterManager) {
        this.apiRouterManager = apiRouterManager;
    }

    @Override
    public Server getServer() {
        return server;
    }

    public void setServer (Server server) {
        this.server = server;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager (PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
    @Override
    public PluginDescription getDescription () {
        return description;
    }

    public void setDescription (PluginDescription pluginDescription) {
        this.description = pluginDescription;
    }
    
    @Override
    public File sourceFile () {
        return sourceFile;
    }
    
    public void setSourceFile (File file) {
        this.sourceFile = file;
    }

    @Override
    public boolean isEnabled () {
        return isEnabled;
    }

    @Override
    public PluginClassLoader getClassLoader () {
        return classLoader;
    }

    public void setClassLoader (PluginClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public CommandManager getCommandManager () {
        return commandManager;
    }

    public void setCommandManager (CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void saveResource (String path, boolean replace) {
        System.out.println(dataFolder);
        if (dataFolder.mkdir()) logger.info("正在创建数据文件夹 {}", dataFolder.getPath());
        if (!replace) {
            File file = new File(dataFolder, path);
            if (file.exists()) return;
        }
        try (InputStream stream = classLoader.getResourceAsStream(path)) {
            File target = new File(dataFolder, path);
            if (target.createNewFile()) logger.debug("正在创建目标文件");
            if (stream != null) {
                Files.copy(stream, new File(dataFolder, path).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                logger.warn("无法为插件 {} 保存文件 {}，因为插件jar中没有此文件！", description.name(), path);
            }
        } catch (IOException e) {
            logger.warn("无法为插件 {} 保存文件 {}，因为 ", description.name(), path, e);
        }
    }

    private void cleanup() {
        this.logger = null;
        this.dataFolder = null;
        this.apiRouterManager = null;
        this.server = null;
        this.pluginManager = null;
        this.description = null;
        this.sourceFile = null;
        try {
            this.classLoader.close();
        } catch (IOException e) {
            logger.error("关闭类加载器时发生异常 ", e);
        }
        this.classLoader = null;
    }
}
