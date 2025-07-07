package com.eternalstarmc.modulake.api.plugin;

import java.util.List;

public record PluginDescription(String name, String version, String description, String apiVersion, List<String> dependencies) {
}
