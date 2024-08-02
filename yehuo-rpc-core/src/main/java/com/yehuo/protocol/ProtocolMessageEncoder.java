package com.yehuo.protocol;


import com.yehuo.serializer.Serializer;
import com.yehuo.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 协议消息编码器
 */

public class ProtocolMessageEncoder {

    /**
     * 编码
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage.getHeader() == null || protocolMessage == null){
            return Buffer.buffer();
        }

        ProtocolMessage.Header header = protocolMessage.getHeader();
        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        // 魔数，保证安全性
        buffer.appendByte(header.getMagic());
        // 版本号
        buffer.appendByte(header.getVersion());
        // 序列化器
        buffer.appendByte(header.getSerializer());
        // 消息类型（请求 / 响应）
        buffer.appendByte(header.getType());
        // 状态
        buffer.appendByte(header.getStatus());
        // 请求 id
        buffer.appendLong(header.getRequestId());

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null){
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());
        // 写入body长度和数据
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
