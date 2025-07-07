package com.eternalstarmc.modulake.api.plugin;

import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;
import com.eternalstarmc.modulake.api.network.Server;
import org.slf4j.Logger;

import java.io.File;
import java.net.URLClassLoader;

public interface PluginBase {
    Logger getLogger ();
    File getDataFolder ();
    ApiRouterManager getApiRouterManager ();
    Server getServer ();
    PluginManager getPluginManager ();
    PluginDescription getDescription ();
    File sourceFile ();
    boolean isEnabled ();
    PluginClassLoader getClassLoader ();
    CommandManager getCommandManager ();
}
