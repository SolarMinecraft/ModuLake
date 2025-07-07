package com.eternalstarmc.modulake.api.plugin;

import java.io.File;
import java.util.List;

public interface PluginManager {
    void loadPlugin (File file);
    void loadPluginsInFolder (File folder);
    void unloadPlugin (PluginBase pluginBase);
    void disablePlugin (PluginBase pluginBase);
    void enablePlugin (PluginBase pluginBase);
    void reloadPlugin (PluginBase pluginBase);
    List<PluginBase> getPlugins ();
    PluginBase getPlugin (String pluginName);
}
