package com.eternalstarmc.modulake.network.routers;

import com.eternalstarmc.modulake.api.network.ApiRouter;
import com.eternalstarmc.modulake.api.network.ResponseData;
import com.eternalstarmc.modulake.api.network.RoutingData;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import io.vertx.core.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.eternalstarmc.modulake.Main.PLUGIN_MANAGER;

public class ServerInfoRouter extends ApiRouter {
    public ServerInfoRouter() {
        super("server_info", HttpMethod.GET);
    }

    @Override
    public ResponseData handler(RoutingData data, HttpMethod method) {
        return new ResponseData(200, Map.of("plugins", getPluginName(PLUGIN_MANAGER.getPlugins()),
                "system", System.getProperty("os.name") + " / " + System.getProperty("os.arch"),
                "java", System.getProperty("java.version")));
    }

    public List<String> getPluginName (List<PluginBase> plugins) {
        List<String> re = new ArrayList<>();
        for (PluginBase plugin : plugins) {
            re.add(plugin.getDescription().name());
        }
        return re;
    }
}
