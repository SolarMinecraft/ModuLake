package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.ApiRouter;
import com.eternalstarmc.modulake.api.network.ResponseData;
import com.eternalstarmc.modulake.api.network.RoutingData;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

import static com.eternalstarmc.modulake.Main.API_ROUTER_MANAGER;
import static com.eternalstarmc.modulake.api.StaticValues.GSON;

public class ApiRoutHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext context) {
        if (context.request().path().equals("/api") || context.request().path().equals("/api/")) {
            HttpServerResponse response = context.response();
            response.putHeader("Content-Type", "application/json; Charset=UTF-8");
            response.setStatusCode(404).end(GSON.toJson(Map.of("response", "failed",
                    "msg", "Not found!")));
        } else {
            HttpServerResponse response = context.response();
            response.putHeader("Content-Type", "application/json; Charset=UTF-8");
            String[] paths = context.request().path().split("/");
            if (paths.length < 3) {
                response.setStatusCode(404).end(GSON.toJson(Map.of("response", "failed",
                        "msg", "Not found!")));
                return;
            }
            String handlerName = paths[2];
            ApiRouter router = API_ROUTER_MANAGER.routers.get(handlerName);
            int code = 404;
            Map<String, Object> data;
            HttpMethod method = context.request().method();
            if (router == null) {
                data = Map.of("response", "failed",
                        "msg", "Not found!");
            }
            else {
                if (List.of(router.getMethods()).contains(method)) {
                    ResponseData data1 = router.handler(new RoutingData(context), context.request().method());
                    data = data1.data();
                    code = data1.code();
                } else {
                    code = 405;
                    data = Map.of("response", "failed",
                            "msg", "Method not allowed!");
                }
            }
            response.setStatusCode(code).end(GSON.toJson(data));
        }
    }
}
