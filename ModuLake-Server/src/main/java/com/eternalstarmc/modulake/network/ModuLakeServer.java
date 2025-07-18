package com.eternalstarmc.modulake.network;

import com.eternalstarmc.modulake.api.StaticValues;
import com.eternalstarmc.modulake.api.network.Server;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static com.eternalstarmc.modulake.Main.CONFIG;
import static com.eternalstarmc.modulake.Main.PLACE_HOLDER_MANAGER;

public class ModuLakeServer extends StaticValues {
    private final String host;
    private final int port;
    private final InetAddress address;
    private final HttpServer httpServer;
    private final Router router;
    private final Router apiRouter;
    private final Server server;
    private final boolean sslEnabled;
    private static final Logger log = LoggerFactory.getLogger(ModuLakeServer.class);

    public ModuLakeServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.address = new InetSocketAddress(this.host, this.port).getAddress();
        router = Router.router(VERTX);
        apiRouter = Router.router(VERTX);
        CorsHandler corsHandler = CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowedHeader("origin")
                .allowedHeader("content-type")
                .allowedHeader("accept")
                .allowedHeader("x-requested-with")
                .allowedHeader("access-control-allow-credentials")
                .allowedHeader("access-control-allow-headers")
                .allowedHeader("Authorization");

        router.route().handler(corsHandler);
        apiRouter.route().handler(BodyHandler.create("uploads"));
        router.mountSubRouter("/api", apiRouter);
        ApiRoutHandler apiRoutHandler = new ApiRoutHandler();
        apiRouter.route().handler(apiRoutHandler);
        router.get("/").handler(this::handler);
        httpServer = VERTX.createHttpServer();
        server = new ServerImpl(this.host, this.port, this.address);
        sslEnabled = CONFIG.getBoolean("ssl.enable");
        if (sslEnabled) {
            String key = PLACE_HOLDER_MANAGER.replacePlaceHolders(CONFIG.getString("ssl.pem"));
            String cert = PLACE_HOLDER_MANAGER.replacePlaceHolders(CONFIG.getString("ssl.cert"));
        }
    }

    public Future<HttpServer> start() {
        return httpServer.requestHandler(router).listen(port, host).onComplete(result -> {
            Thread.currentThread().setName("SERVER-THREAD");
            if (result.succeeded()) {
                log.info("服务器已成功启动！");
            } else {
                log.error("服务器无法正常启动：", result.cause());
            }
        });
    }

    private void handler (RoutingContext context) {
        HttpServerResponse response = context.response();
        response.putHeader("Content-Type", "text/html; charset=UTF-8")
                .end("<h1>ModuLake is running!</h1></br><h1>ModuLake正处于运行状态！</h1>");
    }

    public Router getApiRouter () {
        return apiRouter;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Server getServer() {
        return server;
    }

    public void stop() {
        httpServer.close();
        System.out.println("Http服务器已停止监听");
    }
}
