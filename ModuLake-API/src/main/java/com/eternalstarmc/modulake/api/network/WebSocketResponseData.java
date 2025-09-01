package com.eternalstarmc.modulake.api.network;

public class WebSocketResponseData {
    private final String data;

    public WebSocketResponseData (String data) {
        this.data = data;
    }

    public String getData () {
        return data;
    }

    @Override
    public String toString () {
        return data;
    }
}
