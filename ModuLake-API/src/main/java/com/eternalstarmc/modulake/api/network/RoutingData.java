package com.eternalstarmc.modulake.api.network;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public record RoutingData(RoutingContext context) {

    public HttpServerRequest request() {
        return context.request();
    }

    public HttpServerResponse response() {
        return context.response();
    }

    public String path() {
        return context.normalizedPath();
    }

    public List<String> queryParam(String name) {
        return context.queryParam(name);
    }

    public String getHeader(String headerName) {
        return context.request().getHeader(headerName);
    }


    public <T> T get(String key) {
        return context.get(key);
    }

    public RoutingContext put(String key, Object value) {
        context.put(key, value);
        return context;
    }
}
