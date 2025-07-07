package com.eternalstarmc.modulake.command.commands;

import com.eternalstarmc.modulake.api.commands.CommandHandler;
import com.eternalstarmc.modulake.api.commands.CommandSender;
import com.eternalstarmc.modulake.command.CommandManagerImpl;

import static com.eternalstarmc.modulake.Main.COMMAND_MANAGER;

public class HelpCommandHandler extends CommandHandler {
    @Override
    public void handler(CommandSender sender, String commandName, String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n可用的命令：\n");
        ((CommandManagerImpl) COMMAND_MANAGER).getCommands().forEach((nameSpace, command) -> builder.append(nameSpace).append(" | ").append(command.getDescription()).append("\n"));
        sender.sendMessage(builder.toString());
    }
}
