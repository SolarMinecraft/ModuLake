package com.eternalstarmc.modulake.api.network;

import io.vertx.core.http.HttpMethod;

public abstract class ApiRouter {
    private final String rout;
    private final HttpMethod[] methods;

    public ApiRouter (String rout, HttpMethod... methods) {
        this.rout = rout.startsWith("/") ? rout.substring(1) : rout;
        this.methods = methods;
    }

    public abstract void handler (RoutingData data, HttpMethod method);

    public String getRout() {
        return rout;
    }

    public HttpMethod[] getMethods() {
        return methods;
    }
}
