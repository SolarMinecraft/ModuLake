package com.eternalstarmc.modulake.api.commands;

import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.utils.NameSpace;

public interface Command {
    NameSpace getNameSpace();

    void execute(CommandSender sender, String commandName, String[] args);

    void setHandler (CommandHandler handler);

    String getName ();

    String[] getAliases ();

    PluginBase getPlugin ();

    CommandHandler getHandler ();

    String getDescription ();
}
