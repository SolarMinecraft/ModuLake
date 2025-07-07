package com.eternalstarmc.modulake.command;

import com.eternalstarmc.modulake.api.commands.Command;
import com.eternalstarmc.modulake.api.commands.CommandHandler;
import com.eternalstarmc.modulake.api.commands.CommandSender;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.utils.NameSpace;

public class CommandImpl implements Command {
    private final NameSpace nameSpace;
    private final String name;
    private final String[] aliases;
    private final String description;
    private CommandHandler handler = null;


    public CommandImpl (PluginBase plugin, String name, String[] aliases, String description) {
        this.nameSpace = new NameSpace(plugin, name);
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    @Override
    public NameSpace getNameSpace() {
        return nameSpace;
    }

    @Override
    public void execute(CommandSender sender, String commandName, String[] args) {
        if (handler == null) return;
        handler.handler(sender, commandName, args);
    }

    @Override
    public void setHandler(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public PluginBase getPlugin() {
        return nameSpace.pluginBase();
    }

    @Override
    public CommandHandler getHandler() {
        return handler;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
