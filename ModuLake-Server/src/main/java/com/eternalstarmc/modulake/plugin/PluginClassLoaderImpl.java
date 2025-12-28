package com.eternalstarmc.modulake.plugin;

import com.eternalstarmc.modulake.Main;
import com.eternalstarmc.modulake.api.Impl;
import com.eternalstarmc.modulake.api.plugin.AbsPlugin;
import com.eternalstarmc.modulake.api.plugin.PluginClassLoader;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Impl("PLUGIN_SYSTEM, PluginClassLoaderImpl")
public class PluginClassLoaderImpl extends PluginClassLoader {
    private final Map<AbsPlugin, PluginClassLoaderImpl> dependencies = new ConcurrentHashMap<>();


    public PluginClassLoaderImpl(URL[] urls) {
        super(urls, Main.class.getClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        try {
            loadedClass = super.findClass(name);
        } catch (ClassNotFoundException ignored) {}
        if (loadedClass == null) {
            for (PluginClassLoaderImpl clazzLoader : dependencies.values()) {
                try {
                    loadedClass = clazzLoader.findClass0(name);
                    if (loadedClass != null) break;
                } catch (ClassNotFoundException ignored) {
                }
            }
        }

        if (loadedClass == null) {
            throw new ClassNotFoundException(name);
        }
        return loadedClass;
    }

    protected Class<?> findClass0 (String name) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        return super.findClass(name);
    }

    public void registerDependency (AbsPlugin plugin, PluginClassLoaderImpl classLoader) {
        this.dependencies.put(plugin, classLoader);
    }

    public void registerDependency (AbsPlugin plugin) {
        registerDependency(plugin, (PluginClassLoaderImpl) plugin.getClassLoader());
    }

    public void registerDependencies (AbsPlugin... plugins) {
        for (AbsPlugin plugin : plugins) {
            registerDependency(plugin);
        }
    }
}
