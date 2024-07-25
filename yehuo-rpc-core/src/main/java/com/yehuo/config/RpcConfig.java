package com.yehuo.config;


import lombok.Data;

@Data
public class RpcConfig {

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
    private int serverPort = 8080;

}
