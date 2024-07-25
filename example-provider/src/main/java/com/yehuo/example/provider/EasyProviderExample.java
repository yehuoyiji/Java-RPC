package com.yehuo.example.provider;

import com.yehuo.RpcApplication;
import com.yehuo.example.common.service.UserService;
import com.yehuo.register.LocalRegistry;
import com.yehuo.server.HttpServer;
import com.yehuo.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动Web服务器
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
