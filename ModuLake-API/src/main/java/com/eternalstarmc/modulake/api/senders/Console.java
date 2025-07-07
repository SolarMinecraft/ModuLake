package com.eternalstarmc.modulake.api.senders;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.commands.CommandSender;
import com.eternalstarmc.modulake.api.utils.NameSpace;

public abstract class Console extends CommandSender {
    public Console() {
        super("Console", new NameSpace(ModuLake.getKernel(), "console_command_sender"));
    }
}
