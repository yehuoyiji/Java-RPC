package com.yehuo.protocol;

import com.yehuo.model.RpcRequest;
import com.yehuo.model.RpcResponse;
import com.yehuo.serializer.Serializer;
import com.yehuo.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 协议消息编码器
 */

public class ProtocolMessageDecoder {

    /**
     * 解码
     * @param buffer
     * @return
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        // 分别从指定位置读出Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        // 校验魔数
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic); // 8bit
        header.setVersion(buffer.getByte(1)); // 8bit
        header.setSerializer(buffer.getByte(2)); // 8bit
        header.setType(buffer.getByte(3)); // 8bit
        header.setStatus(buffer.getByte(4)); // 8bit
        header.setRequestId(buffer.getLong(5)); // Long 8字节 = 64bit
        header.setBodyLength(buffer.getInt(13)); // int 4字节 = 32bit
        // 和为136bit = 136/8 = 17字节
        // 解决粘包问题，只读特定长度数据
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        // 解析消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化消息协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if(messageTypeEnum == null) {
            throw new RuntimeException("序列化消息类型不存在");
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }
    }
}
