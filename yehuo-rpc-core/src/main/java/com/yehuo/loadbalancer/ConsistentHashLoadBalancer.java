package com.yehuo.loadbalancer;

import com.yehuo.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{

    /**
     *一致性hash环，用来存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 每个服务的虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 构建虚拟节点环
        for(ServiceMetaInfo serviceMetaInfo: serviceMetaInfoList) {
            for(int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo + "#" + 1);
                virtualNodes.put(hash, serviceMetaInfo);
            }

        }
        // 获取调用请求的hash
        int hash = getHash(requestParams);
        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry == null) {
            // 如果没有大于等于调用请求hash值的虚拟节点，则返回环首部节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    public Integer getHash(Object key) {
        return key.hashCode();
    }
}
