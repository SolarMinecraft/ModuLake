package com.eternalstarmc.modulake.api.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public abstract class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
