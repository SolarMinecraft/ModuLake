package com.eternalstarmc.modulake.plugin;

import com.eternalstarmc.modulake.Main;
import com.eternalstarmc.modulake.api.StaticValues;
import com.eternalstarmc.modulake.api.exception.ExceptionUtils;
import com.eternalstarmc.modulake.api.exception.PluginLoadingException;
import com.eternalstarmc.modulake.api.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.Main.COMMAND_MANAGER;

public class PluginManagerImpl implements PluginManager {
    private static final Logger log = LoggerFactory.getLogger(PluginManagerImpl.class);

    private final Map<String, AbsPlugin> plugins = new ConcurrentHashMap<>();
    private final Map<AbsPlugin, PluginClassLoader> loaders = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void loadPlugin(File file) {
        try {
            PluginClassLoaderImpl clazzLoader = new PluginClassLoaderImpl(new URL[] {file.toURI().toURL()});
            try (InputStream stream = clazzLoader.getResourceAsStream("plugin.yml")) {
                if (stream == null) return;
                Yaml YAML = StaticValues.YAML_THREAD_LOCAL.get();
                Map<String, Object> plugin$yml = YAML.load(stream);
                String clazzName = plugin$yml.get("main-class").toString();
                String pluginName = plugin$yml.get("pluginName").toString();
                String pluginVersion = plugin$yml.get("version").toString();
                String description = plugin$yml.get("description").toString();
                String apiVersion = plugin$yml.get("apiVersion").toString();
                List<String> dependencies;
                if (plugin$yml.get("dependencies") == null) {
                    dependencies = null;
                } else {
                    dependencies = (List<String>) plugin$yml.get("dependencies");
                }
                Class<?> clazz = clazzLoader.loadClass(clazzName);
                AbsPlugin plugin = (AbsPlugin) clazz.getDeclaredConstructor().newInstance();
                if (dependencies != null) {
                    for (String dependency : dependencies) {
                        if (plugins.get(dependency) != null) {
                            clazzLoader.registerDependency(plugins.get(dependency));
                        } else {
                            throw new PluginLoadingException("插件 " + pluginName + " 的依赖未安装！（" + dependency + "）");
                        }
                    }
                }
                plugin.setDescription(new PluginDescription(pluginName, pluginVersion, description, apiVersion, dependencies));
                plugin.setPluginManager(Main.PLUGIN_MANAGER);
                plugin.setLogger(PluginLoggerManager.getPluginLogger(plugin));
                plugin.setApiRouterManager(Main.API_ROUTER_MANAGER);
                plugin.setServer(Main.SERVER.getServer());
                plugin.setDataFolder(new File("./data/" + pluginName));
                plugin.setSourceFile(file);
                plugin.setClassLoader(clazzLoader);
                plugin.setCommandManager(COMMAND_MANAGER);
                plugins.put(pluginName, plugin);
                loaders.put(plugin, clazzLoader);
                plugin.onEnable0();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                ExceptionUtils.handlerException("无法加载插件 " + file.getName() + " ，原因 {}： {}", e);
            }
        } catch (Exception e) {
            ExceptionUtils.handlerException("无法加载插件 " + file.getName() + " ，原因 {}： {}", e);
        }
    }

    @Override
    public void loadPluginsInFolder(File folder) {
        try {
            if (folder.mkdir()) log.info("正在创建插件文件夹 {}", folder.getName());
            File[] files = folder.listFiles();
            if (files == null) return;
            List<File> jarFiles = new ArrayList<>();
            for (File file : files) {
                if (file.getName().endsWith(".jar")) {
                    jarFiles.add(file);
                }
            }
            completeLoadPlugins(preLoadPlugin(jarFiles));
        } catch (Throwable e) {
            log.error("无法加载插件文件夹 {}", folder.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<PreloadPlugin> preLoadPlugin (List<File> files) {
        List<PreloadPlugin> preloadPlugins = new ArrayList<>();
        for (File file : files) {
            try {
                PluginClassLoaderImpl clazzLoader = new PluginClassLoaderImpl(new URL[]{file.toURI().toURL()});
                try (InputStream stream = clazzLoader.getResourceAsStream("plugin.yml")) {
                    if (stream == null) continue;
                    Yaml YAML = StaticValues.YAML_THREAD_LOCAL.get();
                    Map<String, Object> plugin$yml = YAML.load(stream);
                    String pluginName = plugin$yml.get("pluginName").toString();
                    List<String> dependencies;
                    if (plugin$yml.get("dependencies") == null) {
                        dependencies = null;
                    } else {
                        dependencies = (List<String>) plugin$yml.get("dependencies");
                    }
                    if (dependencies != null) {
                        for (String dependency : dependencies) {
                            if (plugins.get(dependency) != null) {
                                clazzLoader.registerDependency(plugins.get(dependency));
                            }
                        }
                    }
                    preloadPlugins.add(new PreloadPlugin(pluginName, clazzLoader, plugin$yml, file));
                }
            } catch (Exception e) {
                ExceptionUtils.handlerException("无法加载插件 " + file.getName() + " ，原因 {}： {}", e);
            }
        }
        return topologicalSort(preloadPlugins);
    }

    @SuppressWarnings("unchecked")
    public void completeLoadPlugins (List<PreloadPlugin> preloadPlugins) {
        for (PreloadPlugin value : preloadPlugins) {
            try {
                Map<String, Object> plugin$yml = value.getPlugin$yml();
                PluginClassLoaderImpl clazzLoader = value.getClassLoader();
                String clazzName = plugin$yml.get("main-class").toString();
                String pluginName = plugin$yml.get("pluginName").toString();
                String pluginVersion = plugin$yml.get("version").toString();
                String description = plugin$yml.get("description").toString();
                String apiVersion = plugin$yml.get("apiVersion").toString();
                List<String> dependencies;
                if (plugin$yml.get("dependencies") == null) {
                    dependencies = null;
                } else {
                    dependencies = (List<String>) plugin$yml.get("dependencies");
                }
                Class<?> clazz = clazzLoader.loadClass(clazzName);
                AbsPlugin plugin = (AbsPlugin) clazz.getDeclaredConstructor().newInstance();
                if (dependencies != null) {
                    for (String dependency : dependencies) {
                        if (plugins.get(dependency) != null) {
                            clazzLoader.registerDependency(plugins.get(dependency));
                        } else {
                            throw new PluginLoadingException("插件 " + pluginName + " 的依赖未安装！（" + dependency + "）");
                        }
                    }
                }
                plugin.setDescription(new PluginDescription(pluginName, pluginVersion, description, apiVersion, dependencies));
                plugin.setPluginManager(Main.PLUGIN_MANAGER);
                plugin.setLogger(PluginLoggerManager.getPluginLogger(plugin));
                plugin.setApiRouterManager(Main.API_ROUTER_MANAGER);
                plugin.setServer(Main.SERVER.getServer());
                plugin.setDataFolder(new File("./data/" + pluginName));
                plugin.setSourceFile(value.file);
                plugin.setClassLoader(clazzLoader);
                plugin.setCommandManager(COMMAND_MANAGER);
                plugins.put(pluginName, plugin);
                loaders.put(plugin, clazzLoader);
                plugin.onEnable0();
            } catch (Throwable e) {
                log.error("在加载插件 {} 时发生错误，", value.name, e);
            }
        }
    }

    @Override
    public void unloadPlugin(PluginBase pluginBase) {
        AbsPlugin plugin = plugins.get(pluginBase.getDescription().name());
        if (plugin == null) return;
        if (plugin.isEnabled()) {
            plugin.onDisable0();
            loaders.remove(plugin);
            plugins.remove(pluginBase.getDescription().name());
        }
    }

    @Override
    public void disablePlugin(PluginBase pluginBase) {
        AbsPlugin plugin = plugins.get(pluginBase.getDescription().name());
        if (plugin == null) return;
        if (plugin.isEnabled()) {
            plugin.onDisable1();
        }
    }

    @Override
    public void enablePlugin(PluginBase pluginBase) {
        AbsPlugin plugin = plugins.get(pluginBase.getDescription().name());
        if (plugin == null) return;
        if (!plugin.isEnabled()) {
            plugin.onEnable0();
        }
    }

    @Override
    public void reloadPlugin(PluginBase pluginBase) {
        disablePlugin(pluginBase);
        enablePlugin(pluginBase);
    }

    @Override
    public List<PluginBase> getPlugins() {
        return new ArrayList<>(plugins.values());
    }

    @Override
    public PluginBase getPlugin(String pluginName) {
        return plugins.get(pluginName);
    }

    public Map<String, AbsPlugin> getPluginsMap () {
        return plugins;
    }

    public Map<AbsPlugin, PluginClassLoader> getLoaders () {
        return loaders;
    }

    public void cleanup() {
        plugins.forEach(((s, absPlugin) -> {
            if (!s.equals("ModuLake")) {
                absPlugin.onDisable0();
            }
        }));
        loaders.clear();
        plugins.clear();
    }

    public static List<PreloadPlugin> topologicalSort(List<PreloadPlugin> plugins) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, PreloadPlugin> loaders = new HashMap<>();
        for (PreloadPlugin plugin : plugins) {
            loaders.put(plugin.getName(), plugin);
        }

        for (PreloadPlugin plugin : plugins) {
            graph.putIfAbsent(plugin.name, new ArrayList<>());
            inDegree.putIfAbsent(plugin.name, 0);
            for (String dependency : plugin.getDependencies()) {
                graph.putIfAbsent(dependency, new ArrayList<>());
                graph.get(dependency).add(plugin.name);
                inDegree.put(plugin.name, inDegree.get(plugin.name) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<PreloadPlugin> sortedPlugins = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            sortedPlugins.add(loaders.get(current));

            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedPlugins.size() != plugins.size()) {
            throw new RuntimeException("存在循环依赖，无法进行拓扑排序");
        }

        return sortedPlugins;
    }

    public static class PreloadPlugin {
        private final List<String> dependencies = new ArrayList<>();
        private final String name;
        private final PluginClassLoaderImpl classLoader;
        private final Map<String, Object> plugin$yml;
        private final File file;


        public PreloadPlugin(String name, PluginClassLoaderImpl classLoader, Map<String, Object> plugin$yml, File file) {
            this.name = name;
            this.classLoader = classLoader;
            this.plugin$yml = plugin$yml;
            this.file = file;
        }

        public String getName() {
            return name;
        }

        public PluginClassLoaderImpl getClassLoader() {
            return classLoader;
        }

        public List<String> getDependencies () {
            return dependencies;
        }

        public Map<String, Object> getPlugin$yml() {
            return plugin$yml;
        }

        public File getFile() {
            return file;
        }
    }
}
