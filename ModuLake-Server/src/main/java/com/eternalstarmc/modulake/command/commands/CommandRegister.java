package com.eternalstarmc.modulake.command.commands;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.commands.Command;
import com.eternalstarmc.modulake.api.commands.CommandHandler;
import com.eternalstarmc.modulake.api.plugin.PluginBase;

import static com.eternalstarmc.modulake.Main.COMMAND_MANAGER;

public class CommandRegister {
    public static void init () {
        PluginBase kernel = ModuLake.getKernel();
        registerCommand(kernel, "help",
                "ModuLake官方提供的帮助指令", new HelpCommandHandler(), "?");
        registerCommand(kernel, "exit",
                "ModuLake官方提供的关闭指令", new ExitCommandHandler(), "stop", "end");
        registerCommand(kernel, "reload",
                "ModuLake官方提供的重载指令", new ReloadCommandHandler(), "r", "rl");
    }

    public static void registerCommand (PluginBase pluginBase, String name, String description, CommandHandler handler, String... aliases) {
        COMMAND_MANAGER.newCommand(pluginBase, name, aliases, description).setHandler(handler);
    }
}
