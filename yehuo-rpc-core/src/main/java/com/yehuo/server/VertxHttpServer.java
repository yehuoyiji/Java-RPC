package com.yehuo.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    public void doStart(int port) {
        // 创建Vert.x 实例
        Vertx vertx = Vertx.vertx();
        // 创建HTTP服务器，但此时服务器还没有开始监听任何端口。
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        // 设置指定请求处理器进行请求的处理
        server.requestHandler(new HttpServerHandler());
        // 启动 HTTP 服务器并监听指定端口
        server.listen(port, result -> {
            if(result.succeeded()) {
                System.out.println("服务器现在监听端口：" + port);
            }else {
                System.out.println("服务器启动失败：" + result.cause());
            }
        });

    }
}
