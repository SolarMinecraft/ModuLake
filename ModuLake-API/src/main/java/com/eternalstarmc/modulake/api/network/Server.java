package com.eternalstarmc.modulake.api.network;

import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.plugin.PluginManager;

import java.net.InetAddress;
import java.util.List;

public interface Server {
    String host();
    int port();
    InetAddress listeningAddress();
    PluginBase getPlugin (String pluginName);
    List<PluginBase> getPlugins ();
    PluginManager getPluginManager ();
}
