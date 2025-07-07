package com.eternalstarmc.modulake.command;

import com.eternalstarmc.modulake.api.commands.Command;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.senders.Console;
import com.eternalstarmc.modulake.api.utils.NameSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.eternalstarmc.modulake.Main.COMMAND_MANAGER;

public class ConsoleImpl extends Console {
    private final UUID uuid;
    private static final Logger logger = LoggerFactory.getLogger(ConsoleImpl.class);


    public ConsoleImpl () {
        this.uuid = new UUID(0L, 0L);
    }

    @Override
    public void performCommand(Command command, String commandName, String[] args) {
        command.execute(this, commandName, args);
    }

    @Override
    public void performCommand(NameSpace nameSpace, String commandName, String[] args) {
        performCommand(COMMAND_MANAGER.getCommand(nameSpace), commandName, args);
    }

    @Override
    public void performCommand(PluginBase plugin, String name, String commandName, String[] args) {
        performCommand(new NameSpace(plugin, name), commandName, args);
    }

    @Override
    public void sendMessage(String message) {
        logger.info(message);
    }


    //命令行终端具有至高无上的权限（bushi
    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    //同上
    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }
}
