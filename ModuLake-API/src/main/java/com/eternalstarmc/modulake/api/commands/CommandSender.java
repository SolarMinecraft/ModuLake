package com.eternalstarmc.modulake.api.commands;

import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.utils.NameSpace;

import java.util.UUID;

public abstract class CommandSender {
    private final String name;
    private final NameSpace nameSpace;

    public CommandSender (String name, NameSpace nameSpace) {
        this.name = name;
        this.nameSpace = nameSpace;
    }

    public abstract void performCommand (Command command, String commandName, String[] args);

    public abstract void performCommand (NameSpace nameSpace, String commandName, String[] args);

    public abstract void performCommand (PluginBase plugin, String name, String commandName, String[] args);

    public abstract void sendMessage(String message);

    public abstract boolean hasPermission(String permission);

    public abstract boolean isOp();

    public abstract UUID getUniqueId();

    public String getName() {
        return name;
    }

    public NameSpace getNameSpace() {
        return nameSpace;
    }
}
