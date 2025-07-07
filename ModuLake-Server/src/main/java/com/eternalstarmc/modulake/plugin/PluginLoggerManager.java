package com.eternalstarmc.modulake.plugin;

import com.eternalstarmc.modulake.api.plugin.PluginBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginLoggerManager {
    public static Logger getPluginLogger (PluginBase pluginBase) {
        String name = pluginBase.getDescription().name();
        return new SLF4JPluginLogger(LoggerFactory.getLogger(pluginBase.getClass()), "[" + name + "] ");
    }
}
