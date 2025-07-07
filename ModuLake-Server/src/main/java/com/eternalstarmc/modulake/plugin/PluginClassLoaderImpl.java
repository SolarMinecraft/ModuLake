package com.eternalstarmc.modulake.plugin;

import com.eternalstarmc.modulake.Main;
import com.eternalstarmc.modulake.api.plugin.AbsPlugin;
import com.eternalstarmc.modulake.api.plugin.PluginClassLoader;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.Main.PLUGIN_MANAGER;

public class PluginClassLoaderImpl extends PluginClassLoader {
    private final Map<AbsPlugin, PluginClassLoaderImpl> dependencies = new ConcurrentHashMap<>();


    public PluginClassLoaderImpl(URL[] urls) {
        super(urls, Main.class.getClassLoader());
    }

    @Override
    protected Class<?> findClass (String name) throws ClassNotFoundException {
        try {return super.findClass(name);} catch (ClassNotFoundException ignored) {}

        PluginManagerImpl manager = (PluginManagerImpl) PLUGIN_MANAGER;
        for (AbsPlugin absPlugin : manager.getPluginsMap().values()) {
            PluginClassLoaderImpl loader = (PluginClassLoaderImpl) absPlugin.getClassLoader();
            return loader.findClass0(name);
        }
        throw new ClassNotFoundException(name);
    }

    protected Class<?> findClass0 (String name) throws ClassNotFoundException {
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
