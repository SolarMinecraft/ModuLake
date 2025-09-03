package com.eternalstarmc.modulake.api.network;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;

import java.util.Map;

import static com.eternalstarmc.modulake.api.StaticValues.GSON;

public abstract class SimpleApiRouter extends ApiRouter {
    public SimpleApiRouter(String rout, HttpMethod... methods) {
        super(rout, methods);
    }

    @Override
    public void handler(RoutingData data, HttpMethod method) {
        ResponseData data1 = handler0(data, method);
        Map<String, Object> data2 = data1.data();
        int code = data1.code();
        HttpServerResponse response = data.response();
        response.putHeader("Content-Type", "application/json; Charset=UTF-8");
        response.setStatusCode(code).end(GSON.toJson(data2));
    }

    public abstract ResponseData handler0 (RoutingData data, HttpMethod method);
}
