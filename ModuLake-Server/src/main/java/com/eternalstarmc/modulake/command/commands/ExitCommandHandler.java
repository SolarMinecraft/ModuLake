package com.eternalstarmc.modulake.command.commands;

import com.eternalstarmc.modulake.api.commands.CommandHandler;
import com.eternalstarmc.modulake.api.commands.CommandSender;
import com.eternalstarmc.modulake.plugin.PluginManagerImpl;

import static com.eternalstarmc.modulake.Main.PLUGIN_MANAGER;

public class ExitCommandHandler extends CommandHandler {
    @Override
    public void handler(CommandSender sender, String commandName, String[] args) {
        sender.sendMessage("正在关闭ModuLake！");
        System.exit(0);
    }
}
