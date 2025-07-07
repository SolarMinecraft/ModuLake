package com.eternalstarmc.modulake.command.commands;

import com.eternalstarmc.modulake.Init;
import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.commands.CommandHandler;
import com.eternalstarmc.modulake.api.commands.CommandSender;

public class ReloadCommandHandler extends CommandHandler {
    @Override
    public void handler(CommandSender sender, String commandName, String[] args) {
        if (sender.hasPermission("modulake.admin.reload")) {
            sender.sendMessage("正在重载ModuLake " + ModuLake.apiVersion);
            Init.cleanup();
            Init.init();
            sender.sendMessage("重载完成！");
        } else {
            sender.sendMessage("你没有权限使用此命令！");
        }
    }
}
