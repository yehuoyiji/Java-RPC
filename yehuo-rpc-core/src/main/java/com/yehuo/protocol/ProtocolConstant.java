package com.yehuo.protocol;

/**
 * 协议常量
 *

 */
public interface ProtocolConstant {

    /**
     * 消息头长度 17字节
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数 1字节 = 8bit
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号 1字节 = 8bit
     */
    byte PROTOCOL_VERSION = 0x1;
}
