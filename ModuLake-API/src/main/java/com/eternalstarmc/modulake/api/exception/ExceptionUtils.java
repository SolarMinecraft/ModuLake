package com.eternalstarmc.modulake.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eternalstarmc.modulake.api.StaticValues.EVENT_BUS;

public class ExceptionUtils {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void handlerException (String msg, Throwable cause) {
        log.error(msg, cause, cause.getClass().getName(), cause.getMessage());
        EVENT_BUS.publish("server.exception", new Throwable(cause));
    }
}
