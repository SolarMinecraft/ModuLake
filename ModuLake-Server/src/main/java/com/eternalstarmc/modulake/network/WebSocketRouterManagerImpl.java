package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.WebSocketApiRouter;
import com.eternalstarmc.modulake.api.network.WebSocketBasicRouter;
import com.eternalstarmc.modulake.api.network.WebSocketRouterManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketRouterManagerImpl implements WebSocketRouterManager {
    protected final Map<String, WebSocketApiRouter> wsApiRouters = new ConcurrentHashMap<>();
    protected final Map<String, WebSocketBasicRouter> wsBasicRouters = new ConcurrentHashMap<>();

    public void cleanup () {
        wsApiRouters.clear();
        wsBasicRouters.clear();
    }

    @Override
    public void registerWebSocketRouter(WebSocketApiRouter router) {
        wsApiRouters.put(router.getRoute(), router);
    }

    @Override
    public void registerWebSocketRouter(WebSocketBasicRouter router) {
        wsBasicRouters.put(router.getRoute(), router);
    }
}
