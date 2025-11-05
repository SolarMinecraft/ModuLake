package com.eternalstarmc.modulake.api.network;

import com.eternalstarmc.modulake.api.API;
import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

@API("ABS, WebSocketBasicRouter")
public abstract class WebSocketBasicRouter {
    private final String route;

    public WebSocketBasicRouter(String route) {
        this.route = route.startsWith("/") ? route.substring(1) : route;
    }

    public abstract Buffer handler (ByteBuf buffer);

    public String getRoute() {
        return route;
    }
}
