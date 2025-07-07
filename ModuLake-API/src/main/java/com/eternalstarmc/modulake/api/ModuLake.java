package com.eternalstarmc.modulake.api;

import com.eternalstarmc.modulake.api.plugin.AbsPlugin;
import com.eternalstarmc.modulake.api.plugin.PluginDescription;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModuLake {
    private static final ModuLakePlugin modulakeApiKernel = new ModuLakePlugin();
    private static boolean state = false;
    public static final String apiVersion = "Modulake-API Snapshot 1.0";

    public static ModuLakePlugin getKernel () {
        return modulakeApiKernel;
    }

    public static void setState (boolean status) {
        if (state) throw new IllegalStateException("当前已完成初始化，初始化函数已被禁止使用！");
        state = status;
    }

    public static class ModuLakePlugin extends AbsPlugin {
        private Logger logger;
        private final String name;

        public ModuLakePlugin () {
            this.name = "ModuLake";
            this.setDescription(new PluginDescription(name,
                    apiVersion,
                    "ModuLake api核心模块",
                    apiVersion,
                    new ArrayList<>()));
            this.setDataFolder(new File("./data/" + name));
            this.setSourceFile(null);
            this.setClassLoader(null);
        }

        @Override
        public void onEnable() {
            if (isEnabled()) return;
            logger = getLogger();
            logger.info("正在启动API模块内核……");
        }

        @Override
        public void onDisable() {
            if (!isEnabled()) return;
            logger.info("正在关闭API模块内核……");
        }

        public String getName() {
            return name;
        }
    }
}
