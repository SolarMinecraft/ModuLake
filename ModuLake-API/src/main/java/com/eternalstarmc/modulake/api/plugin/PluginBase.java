package com.eternalstarmc.modulake.api.plugin;

import com.eternalstarmc.modulake.api.API;
import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;
import com.eternalstarmc.modulake.api.network.Server;
import org.slf4j.Logger;

import java.io.File;


@API
public interface PluginBase {

    @API
    Logger getLogger ();
    @API
    File getDataFolder ();
    @API
    ApiRouterManager getApiRouterManager ();
    @API
    Server getServer ();
    @API
    PluginManager getPluginManager ();
    @API
    PluginDescription getDescription ();
    @API
    File sourceFile ();
    @API
    boolean isEnabled ();
    @API
    PluginClassLoader getClassLoader ();
    @API
    CommandManager getCommandManager ();
}
