package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.Impl;
import com.eternalstarmc.modulake.api.network.ApiRouter;
import com.eternalstarmc.modulake.api.network.ApiRouterManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Impl("NETWORK, ApiRouterManagerImpl")
public class ApiRouterManagerImpl implements ApiRouterManager {
    protected final Map<String, ApiRouter> routers = new ConcurrentHashMap<>();

    public void cleanup () {
        routers.clear();
    }

    @Override
    public void registerApiRouter (ApiRouter router) {
        routers.put(router.getRout(), router);
    }
}
