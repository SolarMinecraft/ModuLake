package com.eternalstarmc.modulake.api.config;

import com.eternalstarmc.modulake.api.API;
import com.eternalstarmc.modulake.api.placeholder.PlaceHolderYamlCore;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;

@API
public interface ConfigManager {
    @API
    YamlCore load (String name, boolean stopServerWhenError);
    @API
    YamlCore load (String name);
    @API
    YamlCore load (String name, boolean stopServerWhenError, boolean readCache);
    @API
    PlaceHolderYamlCore loadPlaceholderYamlCore(String name, boolean stopServerWhenError, boolean readCache);
}
