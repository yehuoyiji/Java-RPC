package com.yehuo.serializer;

import cn.hutool.core.util.StrUtil;
import com.yehuo.serializer.JdkSerializer;
import com.yehuo.serializer.Serializer;
import com.yehuo.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;
/**
 * @author 影⼦x
 * @version 1.0
 * @description 序列化器⼯⼚（⽤于获取序列化器对象）
 * @date 2024/3/12 15:14
 */
public class SerializerFactory {
    /**
     * 通过SPI的⽅式获取序列化器
     */
    static {
        SpiLoader.load(Serializer.class);
    }
    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer
            ();
    /**
     * 存储加载后的序列化器
     */
    private static volatile Map<String, Serializer> serializerMap;
    /**
     * 获取实例，没有则给默认JDK序列化器
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        // 懒汉式加载SPI序列化器
        if (serializerMap == null) {
            synchronized (SerializerFactory.class) {
                if (serializerMap == null) {
                    SpiLoader.load(Serializer.class);
                    serializerMap = new HashMap<>();
                }
            }
        }
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
