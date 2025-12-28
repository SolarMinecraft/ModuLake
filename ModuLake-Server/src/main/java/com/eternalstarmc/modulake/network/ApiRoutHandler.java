package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.ApiRouter;
import com.eternalstarmc.modulake.api.network.RoutingData;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
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
            String normalizedPath = context.request().path().replaceAll("/+$", "");
            String[] paths = normalizedPath.split("/");
            if (paths.length < 3) {
                response.setStatusCode(404).end(GSON.toJson(Map.of("response", "failed",
                        "msg", "Not found!")));
                return;
            }
            String handlerName = paths[2];
            ApiRouter router = ((ApiRouterManagerImpl) API_ROUTER_MANAGER).routers.get(handlerName);
            int code = 404;
            Map<String, Object> data;
            HttpMethod method = context.request().method();
            if (router == null) {
                data = Map.of("response", "failed",
                        "msg", "Not found!");
            }
            else {
                if (List.of(router.getMethods()).contains(method)) {
                    router.handler(new RoutingData(context), context.request().method()); // 转交给Router处理
                    return;
                } else {
                    code = 405;
                    data = Map.of("response", "failed",
                            "msg", "Method not allowed!");
                    String allowHeader = String.join(", ", Arrays.stream(router.getMethods()).map(HttpMethod::name).toList());
                    response.putHeader("Allow", allowHeader);
                }
            }
            response.setStatusCode(code).end(GSON.toJson(data));
        }
    }
}
