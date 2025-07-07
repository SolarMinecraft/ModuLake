package com.eternalstarmc.modulake.api.commands;

import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.utils.NameSpace;

public interface CommandManager {

    Command getCommand (NameSpace nameSpace);

    Command newCommand (PluginBase plugin, String name, String description, String... aliases);

    Command newCommand(PluginBase plugin, String name, String[] aliases, String description);
}
