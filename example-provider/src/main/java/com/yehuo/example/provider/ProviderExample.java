package com.yehuo.example.provider;

import cn.hutool.core.net.NetUtil;
import com.yehuo.RpcApplication;
import com.yehuo.config.RegistryConfig;
import com.yehuo.config.RpcConfig;
import com.yehuo.example.common.service.UserService;
import com.yehuo.model.ServiceMetaInfo;
import com.yehuo.register.LocalRegistry;
import com.yehuo.register.Registry;
import com.yehuo.register.RegistryFactory;
import com.yehuo.server.HttpServer;
import com.yehuo.server.VertxHttpServer;
import com.yehuo.server.tcp.VertxTcpServer;


/**
 * 服务提供者示例
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
