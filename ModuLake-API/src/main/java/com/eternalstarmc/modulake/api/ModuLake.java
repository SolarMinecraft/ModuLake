package com.eternalstarmc.modulake.api;

import com.eternalstarmc.modulake.api.dependency.Inject;
import com.eternalstarmc.modulake.api.placeholder.PlaceHolderManager;
import com.eternalstarmc.modulake.api.plugin.AbsPlugin;
import com.eternalstarmc.modulake.api.plugin.PluginDescription;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;

@API
public class ModuLake {
    private static final ModuLakeKernel modulakeApiKernel = new ModuLakeKernel();
    private static boolean state = false;
    public static final String apiVersion = "Modulake-API Snapshot 1.0";
    @Inject
    private static PlaceHolderManager placeHolderManager;

    @API
    public static ModuLakeKernel getKernel () {
        return modulakeApiKernel;
    }

    @API
    public static void setState (boolean status) {
        if (state) throw new IllegalStateException("当前已完成初始化，初始化函数已被禁止使用！");
        state = status;
    }

    @API
    public static PlaceHolderManager getPlaceHolderManager () {
        return placeHolderManager;
    }

    @API
    public static class ModuLakeKernel extends AbsPlugin {
        private Logger logger;
        private final String name;

        @API
        public ModuLakeKernel() {
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
        protected void onEnable() {
            if (isEnabled()) return;
            logger = getLogger();
            logger.info("正在启动API模块内核……");
        }

        @Override
        protected void onDisable() {
            if (!isEnabled()) return;
            logger.info("正在关闭API模块内核……");
        }

        public String getName() {
            return name;
        }
    }
}
