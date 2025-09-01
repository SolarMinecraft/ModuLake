package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.WebSocketApiRouter;
import com.eternalstarmc.modulake.api.network.WebSocketRequestContext;
import com.eternalstarmc.modulake.api.network.WebSocketResponseData;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.SocketAddress;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.api.StaticValues.GSON;
import static com.eternalstarmc.modulake.api.StaticValues.TYPE;

public class WebSocketHandler implements Handler<ServerWebSocket> {
    private final Map<String, WebSocketApiRouter> wsApiRouters = new ConcurrentHashMap<>();
    private final Map<String, ServerWebSocket> swsMap = new ConcurrentHashMap<>();


    @Override
    public void handle(ServerWebSocket sws) {
        sws.accept();
        String path = sws.path();
        if (path.equals("/ws")) {
            sws.handler(buffer -> {
                if (buffer instanceof BufferImpl nBuffer) { // 转换为BufferImpl以获取Netty ByteBuf
                    int packetType = nBuffer.byteBuf().getInt(0);
                    String msg = nBuffer.toString();
                    Map<String, Object> data = GSON.fromJson(msg, TYPE);
                    if (data.get("rout") == null) {
                        sws.write(Buffer.buffer(GSON.toJson(Map.of("response", "failed",
                                "msg", "Bad WS Request!"))));
                        return;
                    }
                    String rout = data.get("rout").toString();
                    WebSocketApiRouter router = wsApiRouters.get(rout);
                    if (router == null) {
                        sws.write(Buffer.buffer(GSON.toJson(Map.of("response", "failed",
                                "msg", "Not Found!"))));
                        return;
                    }
                    data.remove("rout");
                    WebSocketResponseData responseData = router.handler(new WebSocketRequestContext(data));
                    sws.write(Buffer.buffer(responseData.getData()));
                }
            });
            swsMap.put(sws.localAddress().toString(), sws);
            return;
        }
        sws.end(Buffer.buffer(GSON.toJson(Map.of("response", "failed",
                "msg", "Unknown access ws path!"))));
    }

    public void registerWSApiRouter (WebSocketApiRouter router) {
        this.wsApiRouters.put(router.getRout(), router);
    }

    public WebSocketApiRouter getWSApiRouter (String rout) {
        return this.wsApiRouters.get(rout);
    }

    public ServerWebSocket getWebSocketConnection (String address) {
        return swsMap.get(address);
    }

    public ServerWebSocket getWebSocketConnection (SocketAddress address) {
        return swsMap.get(address.toString());
    }
}
