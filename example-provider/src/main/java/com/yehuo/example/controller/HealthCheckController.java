package com.yehuo.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/actuator/health")
    public String healthCheck() {
        // 在这里可以添加其他健康检测逻辑，例如检查数据库连接，第三方服务等。

        // 返回一个简单的健康状态
        return "OK";
    }
}
