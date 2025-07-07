package com.eternalstarmc.modulake.api.senders;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.commands.CommandSender;
import com.eternalstarmc.modulake.api.utils.NameSpace;


public abstract class Browser extends CommandSender {
    public Browser () {
        super("Browser", new NameSpace(ModuLake.getKernel(), "browser_command_sender"));
    }
}
