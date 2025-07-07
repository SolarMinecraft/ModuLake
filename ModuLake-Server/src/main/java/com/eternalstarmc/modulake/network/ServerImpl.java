package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.Server;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.plugin.PluginManager;

import java.net.InetAddress;
import java.util.List;

import static com.eternalstarmc.modulake.Main.PLUGIN_MANAGER;

public record ServerImpl(String host, int port, InetAddress listeningAddress) implements Server {

    @Override
    public PluginBase getPlugin(String pluginName) {
        return PLUGIN_MANAGER.getPlugin(pluginName);
    }

    @Override
    public List<PluginBase> getPlugins() {
        return PLUGIN_MANAGER.getPlugins();
    }

    @Override
    public PluginManager getPluginManager() {
        return PLUGIN_MANAGER;
    }
}
