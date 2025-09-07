package com.eternalstarmc.modulake.api.plugin;

import com.eternalstarmc.modulake.api.API;
import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;
import com.eternalstarmc.modulake.api.network.Server;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@API
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

    @API
    public void onPluginStartup() {
        logger.info("正在启动插件 {} {}", description.name(), description.version());
        onEnable();
        this.isEnabled = true;
    }

    @API
    public void onServerShutdown() {
        logger.info("正在卸载插件 {} {}", description.name(), description.version());
        onDisable();
        this.isEnabled = false;
        cleanup();
    }

    @API
    public void onHotReload() {
        logger.info("正在卸载插件 {} {}", description.name(), description.version());
        onDisable();
        this.isEnabled = false;
    }

    @API
    protected abstract void onEnable ();

    @API
    protected abstract void onDisable ();

    @API
    @Override
    public Logger getLogger() {
        return logger;
    }

    @API
    public void setLogger (Logger logger) {
        this.logger = logger;
    }

    @API
    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @API
    public void setDataFolder (File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @API
    @Override
    public ApiRouterManager getApiRouterManager() {
        return apiRouterManager;
    }

    @API
    public void setApiRouterManager (ApiRouterManager apiRouterManager) {
        this.apiRouterManager = apiRouterManager;
    }

    @API
    @Override
    public Server getServer() {
        return server;
    }

    @API
    public void setServer (Server server) {
        this.server = server;
    }

    @API
    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @API
    public void setPluginManager (PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @API
    @Override
    public PluginDescription getDescription () {
        return description;
    }

    @API
    public void setDescription (PluginDescription pluginDescription) {
        this.description = pluginDescription;
    }

    @API
    @Override
    public File sourceFile () {
        return sourceFile;
    }
    
    public void setSourceFile (File file) {
        this.sourceFile = file;
    }

    @API
    @Override
    public boolean isEnabled () {
        return isEnabled;
    }

    @API
    @Override
    public PluginClassLoader getClassLoader () {
        return classLoader;
    }

    @API
    public void setClassLoader (PluginClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @API
    @Override
    public CommandManager getCommandManager () {
        return commandManager;
    }

    @API
    public void setCommandManager (CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @API
    public void saveResource(String path, boolean replace) {
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
