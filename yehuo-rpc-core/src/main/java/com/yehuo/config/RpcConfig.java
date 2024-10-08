package com.yehuo.config;


import com.yehuo.fault.retry.RetryStrategyKeys;
import com.yehuo.fault.tolerant.TolerantStrategyKeys;
import com.yehuo.loadbalancer.LoadBalanceKeys;
import com.yehuo.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {

    /**
     * 是否开启mock模式 (模拟调用)
     */
    private boolean mock = false;

    /**
     * 名称
     */
    private String name = "yehuo-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务主机号
     */
    private String serverHost = "localhost";

    /**
     * 服务端口号
     */
    private int serverPort = 8085;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalanceKeys.CONSISTENT_HASH;

    /**
     * 重试策略 —— 默认不重试
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略 —— 默认快速失败
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
