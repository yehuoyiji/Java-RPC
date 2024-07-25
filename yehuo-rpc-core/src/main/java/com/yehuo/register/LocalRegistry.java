package com.yehuo.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地服务注册中心
 */
public class LocalRegistry {
    /**
     * 注册中心存储
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName 服务名
     * @param implClass 服务实现类
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务实现类
     * @param serviceName 服务名
     * @return 服务实现类
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 注销服务
     * @param serviceName 服务名
     */
    public static void unregister(String serviceName) {
        map.remove(serviceName);
    }
}
