package com.yehuo.server;

import com.yehuo.model.RpcRequest;
import com.yehuo.model.RpcResponse;
import com.yehuo.register.LocalRegistry;
import com.yehuo.serializer.JdkSerializer;
import com.yehuo.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Http 请求处理
 */

public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        // 记录日志
        System.out.println("收到的请求：" + httpServerRequest.method() + " " + httpServerRequest.uri());

        // 异步处理 HTTP 请求
        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            }catch (Exception e) {
                e.printStackTrace();
            };

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为null, 直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("请求为空");
                doResponse(httpServerRequest, rpcResponse, serializer);
                return;
            }

            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getParameters());
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(httpServerRequest, rpcResponse, serializer);
        });
    }

    /**
     * 处理响应结果
     * @param
     * @param serializer
     * @param rpcResponse
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse,Serializer serializer) {
        HttpServerResponse httpServerResponse =request.response()
                .putHeader("content-type", "application/json");

        try {
            // 序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
