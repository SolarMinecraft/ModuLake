package com.eternalstarmc.modulake.api.config;

import com.eternalstarmc.modulake.api.placeholder.PlaceHolderYamlCore;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;

public interface ConfigManager {
    YamlCore load (String name, boolean stopServerWhenError);
    YamlCore load (String name, boolean stopServerWhenError, boolean readCache);

    PlaceHolderYamlCore loadPlaceholderYamlCore(String name, boolean stopServerWhenError, boolean readCache);
}
