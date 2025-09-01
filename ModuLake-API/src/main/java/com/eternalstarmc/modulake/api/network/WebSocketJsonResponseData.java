package com.eternalstarmc.modulake.api.network;

import java.util.Map;

import static com.eternalstarmc.modulake.api.StaticValues.GSON;

public class WebSocketJsonResponseData extends WebSocketResponseData {
    public WebSocketJsonResponseData(Map<String, Object> json) {
        super(GSON.toJson(json));
    }
}
