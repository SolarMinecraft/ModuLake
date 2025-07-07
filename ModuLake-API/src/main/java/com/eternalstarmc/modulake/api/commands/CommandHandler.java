package com.eternalstarmc.modulake.api.commands;

public abstract class CommandHandler {
    public abstract void handler (CommandSender sender, String commandName, String[] args);
}
