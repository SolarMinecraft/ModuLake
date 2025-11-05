package com.eternalstarmc.modulake.api.network;

public interface WebSocketRouterManager {
    void registerWebSocketRouter (WebSocketApiRouter router);
    void registerWebSocketRouter (WebSocketBasicRouter router);
}
