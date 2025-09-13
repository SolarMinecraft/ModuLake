package com.eternalstarmc.modulake.api.network;

import com.eternalstarmc.modulake.api.API;

@API("ABS, WebSocketApiRouter")
public abstract class WebSocketApiRouter {
    private final String rout;

    public WebSocketApiRouter(String rout) {
        this.rout = rout.startsWith("/") ? rout.substring(1) : rout;
    }

    public abstract WebSocketResponseData handler (WebSocketRequestContext ctx);

    public String getRout() {
        return rout;
    }
}
