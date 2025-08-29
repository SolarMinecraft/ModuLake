package com.eternalstarmc.modulake.config;

import com.eternalstarmc.modulake.api.config.ConfigManager;
import com.eternalstarmc.modulake.api.placeholder.PlaceHolderYamlCore;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;
import com.eternalstarmc.modulake.log.SLF4JPluginLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.Main.CONFIG_FOLDER;

public class ConfigManagerImpl implements ConfigManager {
    private final Map<String, YamlCore> configs = new ConcurrentHashMap<>();
    private static final Logger log = new SLF4JPluginLogger(LoggerFactory.getLogger(ConfigManagerImpl.class), "[ConfigManager] ");

    @Override
    public YamlCore load (String name, boolean stopServerWhenError) {
        return load(name, stopServerWhenError, true);
    }

    @Override
    public YamlCore load (String name, boolean stopServerWhenError, boolean readCache) {
        if (CONFIG_FOLDER.mkdir()) log.info("正在创建配置文件夹...");
        try {
            if (configs.containsKey(name) && readCache) {
                return configs.get(name);
            }
            saveResource(name, false);
            YamlCore yamlCore = new YamlCore(new File(CONFIG_FOLDER, name));
            configs.put(name, yamlCore);
            return yamlCore;
        } catch (Throwable e) {
            log.error("无法加载配置文件 {}！", name, e);
            if (stopServerWhenError) {
                System.out.println("STOPPING SERVER");
                System.exit(1); // 已注册ShutdownHook，所以这会正常关闭服务器
            }
        }
        return null;
    }

    @Override
    public PlaceHolderYamlCore loadPlaceholderYamlCore (String name, boolean stopServerWhenError, boolean readCache) {
        if (CONFIG_FOLDER.mkdir()) log.info("正在创建配置文件夹...");
        try {
            if (configs.containsKey(name) && readCache) {
                YamlCore cache = configs.get(name);
                if (cache instanceof PlaceHolderYamlCore) {
                    return (PlaceHolderYamlCore) configs.get(name);
                } else return null;
            }
            saveResource(name, false);
            PlaceHolderYamlCore yamlCore = new PlaceHolderYamlCore(new File(CONFIG_FOLDER, name));
            configs.put(name, yamlCore);
            return yamlCore;
        } catch (Throwable e) {
            log.error("无法加载配置文件 {}！", name, e);
            if (stopServerWhenError) {
                System.out.println("STOPPING SERVER");
                System.exit(1); // 已注册ShutdownHook，所以这会正常关闭服务器
            }
        }
        return null;
    }

    public void saveResource (String name, boolean replace) {
        File file = new File(CONFIG_FOLDER, name);
        if (!replace && file.exists()) return;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("找不到资源文件 " + name);
            }
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalArgumentException("无法保存资源文件 ", e);
        }
    }
}
