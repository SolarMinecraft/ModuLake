package com.eternalstarmc.modulake.api.exception;

public class PluginLoadingException extends RuntimeException {
    public PluginLoadingException (String msg) {
        super(msg);
    }

    public PluginLoadingException (String msg, Throwable cause) {
        super(msg, cause);
    }
}
