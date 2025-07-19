package com.eternalstarmc.modulake.placeholder.holders;

import com.eternalstarmc.modulake.api.placeholder.PlaceHolder;

import java.io.File;

import static com.eternalstarmc.modulake.Main.PLUGIN_MANAGER;
import static com.eternalstarmc.modulake.Main.SERVER;

public class ServerHolder extends PlaceHolder {
    public ServerHolder() {
        super(
                "server",
                new SubPlaceHolder(
                        "basedir",
                        s -> new File("./").toPath().toString()
                ),
                new SubPlaceHolder(
                        "port",
                        s -> String.valueOf(SERVER.getPort())
                ),
                new SubPlaceHolder(
                        "host",
                        s -> SERVER.getHost()
                ),
                new SubPlaceHolder(
                        "plugins",
                        s -> PLUGIN_MANAGER.getPlugins().toString()
                )
        );
    }
}
