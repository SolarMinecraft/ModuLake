package com.eternalstarmc.modulake.api.network;

import com.eternalstarmc.modulake.api.API;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.plugin.PluginManager;

import java.net.InetAddress;
import java.util.List;

public interface Server {
    @API
    String host();
    @API
    int port();
    @API
    InetAddress listeningAddress();
    @API
    PluginBase getPlugin (String pluginName);
    @API
    List<PluginBase> getPlugins ();
    @API
    PluginManager getPluginManager ();
}
