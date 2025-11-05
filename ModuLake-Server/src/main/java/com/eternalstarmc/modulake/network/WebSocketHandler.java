package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.network.WebSocketApiRouter;
import com.eternalstarmc.modulake.api.network.WebSocketBasicRouter;
import com.eternalstarmc.modulake.api.network.WebSocketRequestContext;
import com.eternalstarmc.modulake.api.network.WebSocketResponseData;
import com.eternalstarmc.modulake.api.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.Main.WEB_SOCKET_ROUTER_MANAGER;
import static com.eternalstarmc.modulake.api.StaticValues.GSON;
import static com.eternalstarmc.modulake.api.StaticValues.TYPE;

public class WebSocketHandler implements Handler<ServerWebSocket> {
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    private final Map<String, ServerWebSocket> swsMap = new ConcurrentHashMap<>();


    @Override
    public void handle(ServerWebSocket sws) {
        sws.accept();
        String path = sws.path();
        if (path.equals("/ws")) {
            log.info("WebSocket客户端 ({}) 已连接至服务器", sws.remoteAddress().toString());
            sws.endHandler(v -> {
                swsMap.remove(sws.remoteAddress().toString());
                log.info("WebSocket远程客户端已断开连接 ({})", sws.remoteAddress().toString());
            });
            sws.handler(buffer -> {
                try {
                    if (buffer instanceof BufferImpl nBuffer) { // 转换为BufferImpl以获取Netty ByteBuf
                        ByteBuf byteB = nBuffer.getByteBuf();
                        int packetType = byteB.readInt(); // 当此变量值为0时，代表这是一个json字符串类型的数据包，当变量值为1时，代表这是一个二进制数据包
                        switch (packetType) {
                            case 0 -> {
                                String msg = ByteBufUtils.readString(byteB);
                                Map<String, Object> data = GSON.fromJson(msg, TYPE);
                                if (data.get("route") == null) {
                                    sws.write(Buffer.buffer().appendInt(0).appendString(GSON.toJson(Map.of("response", "failed",
                                            "msg", "Bad WS Request!"))));
                                    return;
                                }
                                String route = data.get("route").toString();
                                WebSocketApiRouter router = ((WebSocketRouterManagerImpl) WEB_SOCKET_ROUTER_MANAGER).wsApiRouters.get(route);
                                if (router == null) {
                                    sws.write(Buffer.buffer().appendInt(0).appendString(GSON.toJson(Map.of("response", "failed",
                                            "msg", "Not Found!"))));
                                    return;
                                }
                                data.remove("route");
                                WebSocketResponseData responseData = router.handler(new WebSocketRequestContext(data));
                                sws.write(Buffer.buffer().appendInt(0).appendString(responseData.getData()));
                            }
                            case 1 -> {
                                String route = ByteBufUtils.readString(byteB);
                                WebSocketBasicRouter basicRouter = ((WebSocketRouterManagerImpl) WEB_SOCKET_ROUTER_MANAGER).wsBasicRouters.get(route);
                                if (basicRouter == null) {
                                    sws.write(Buffer.buffer().appendInt(0).appendString(GSON.toJson(Map.of("response", "failed",
                                            "msg", "Not Found!"))));
                                    return;
                                }
                                Buffer response = basicRouter.handler(byteB);
                                sws.write(Buffer.buffer().appendInt(1).appendBuffer(response));
                            }
                        }
                    }
                } catch (Exception e) {
                    sws.write(Buffer.buffer().appendInt(0).appendString(GSON.toJson(Map.of("response", "failed",
                            "msg", "Internal server error: " + e.getMessage()))));
                }
            });
            swsMap.put(sws.remoteAddress().toString(), sws);
            return;
        }
        sws.end(Buffer.buffer().appendInt(0).appendString(GSON.toJson(Map.of("response", "failed",
                "msg", "Unknown access ws path!"))));
    }

    public ServerWebSocket getWebSocketConnection (String address) {
        return swsMap.get(address);
    }

    public ServerWebSocket getWebSocketConnection (SocketAddress address) {
        return swsMap.get(address.toString());
    }
}
