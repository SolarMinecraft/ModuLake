package com.eternalstarmc.modulake.api.utils;

import com.eternalstarmc.modulake.api.plugin.PluginBase;

import java.util.Locale;

public class NameSpace {
    private final PluginBase pluginBase;
    private final String name;
    private final String key;

    public NameSpace (PluginBase pluginBase, String name) {
        this.pluginBase = pluginBase;
        this.name = name;
        this.key = pluginBase.getDescription().name().toLowerCase(Locale.ROOT);
    }

    public String name () {
        return name;
    }

    public PluginBase pluginBase () {
        return pluginBase;
    }

    public String getNamespace() {
        return key + ":" + name;
    }

    @Override
    public String toString() {
        return getNamespace();
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return getNamespace().hashCode();
    }
}
