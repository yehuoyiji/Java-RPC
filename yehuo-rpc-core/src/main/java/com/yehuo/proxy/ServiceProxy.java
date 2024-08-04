package com.yehuo.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yehuo.RpcApplication;
import com.yehuo.config.RpcConfig;
import com.yehuo.constant.RpcConstant;
import com.yehuo.model.RpcRequest;
import com.yehuo.model.RpcResponse;
import com.yehuo.model.ServiceMetaInfo;
import com.yehuo.protocol.*;
import com.yehuo.register.Registry;
import com.yehuo.register.RegistryFactory;
import com.yehuo.serializer.Serializer;
import com.yehuo.serializer.SerializerFactory;
import com.yehuo.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 *

 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送HTTP请求
//            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }

            // 发送TCP请求
//            Vertx vertx = Vertx.vertx();
//            NetClient netClient = vertx.createNetClient();
//            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
//            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
//                    result -> {
//                        if (result.succeeded()) {
//                            System.out.println("TCP建立连接成功");
//                            NetSocket socket = result.result();
//                            // 发送数据
//                            // 构造消息
//                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                            ProtocolMessage.Header header = new ProtocolMessage.Header();
//                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(rpcConfig.getSerializer()).getKey());
//                            header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
//                            header.setRequestId(IdUtil.getSnowflakeNextId());
//                            protocolMessage.setHeader(header);
//                            protocolMessage.setBody(rpcRequest);
//                            // 编码请求
//                            try{
//                                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
//                                socket.write(encode);
//                            }catch (IOException e) {
//                                throw new RuntimeException("协议消息编码错误");
//                            }
//
//                            // 接收响应
//                            socket.handler(buffer -> {
//                                try {
//                                    ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                                    responseFuture.complete(decode.getBody());
//                                }catch (IOException e) {
//                                    throw new RuntimeException("协议消息解码错误");
//                                }
//                            });
//                        }else {
//                            System.out.println("TCP建立连接失败");
//                        }
//                    });
//            RpcResponse rpcResponse = responseFuture.get();
//            // 关闭TCP连接
//            netClient.close();
//            System.out.println(rpcResponse.getData());
//            return rpcResponse.getData();
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
