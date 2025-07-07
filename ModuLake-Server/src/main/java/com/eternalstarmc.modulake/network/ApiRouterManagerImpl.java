package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.ApiRouter;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiRouterManagerImpl implements ApiRouterManager {
    protected Map<String, ApiRouter> routers = new ConcurrentHashMap<>();

    public void cleanup () {
        routers.clear();
    }

    @Override
    public void registerApiRouter (ApiRouter router) {
        routers.put(router.getRout(), router);
    }
}
