package com.yehuo.server.tcp;


import com.yehuo.protocol.ProtocolMessageDecoder;
import com.yehuo.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

import java.io.IOException;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // 在这里编写处理请求的逻辑，根据 requestData 构造响应数据并返回
        // 这里只是一个示例，实际逻辑需要根据具体的业务需求来实现
//        return "Hello, client!".getBytes();
        return requestData;
    }

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();
        // 处理请求
        server.connectHandler(new TcpServerHandler());
//
//        server.connectHandler(socket -> {
//            // 处理连接
//            socket.handler(buffer -> {
//                // 处理接收到的字节数组
//                byte[] requestData = buffer.getBytes();
//                // 在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造响应等
//                byte[] responseData = handleRequest(requestData);
//                // 发送响应
//                socket.write(Buffer.buffer(responseData));


//                String testMessage = "Hello, server!Hello, server!Hello, server!";
//                int messageLength = testMessage.length();
//                if(buffer.getBytes().length < messageLength) {
//                    try {
//                        System.out.println("半包， length=" + ProtocolMessageDecoder.decode(buffer) + "/n");
//                    } catch (IOException e) {
//
//                    }
//                    return;
//                }
//                if (buffer.getBytes().length == messageLength) {
//                    try {
//                        System.out.println("完整包， length=" + ProtocolMessageDecoder.decode(buffer) + "/n");
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    socket.write(Buffer.buffer(testMessage));
//                }
//                if (buffer.getBytes().length > messageLength) {
//                    try {
//                        System.out.println("粘包， length=" + ProtocolMessageDecoder.decode(buffer) + "/n");
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    socket.write(Buffer.buffer(testMessage));
//                }

//            });
//        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port,监听端口： " + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8085);
    }
}
