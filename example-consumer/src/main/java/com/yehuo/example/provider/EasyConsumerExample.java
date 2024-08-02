package com.yehuo.example.provider;

import com.yehuo.config.RpcConfig;
import com.yehuo.example.common.model.User;
import com.yehuo.example.common.service.UserService;
import com.yehuo.proxy.ServiceProxyFactory;
import com.yehuo.utils.ConfigUtils;


public class EasyConsumerExample {
    public static void main(String[] args) throws InterruptedException {

        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc.toString());

        // 静态代理
        // UserService userService = new UserServiceProxy();

        User user = new User();
        user.setName("yehuo");

        // 动态代理
        // 测试监听机制 以及 本地缓存是否成功
//        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//        User newUser = userService.getUser(user);
//        UserService Test1 = ServiceProxyFactory.getProxy(UserService.class);
//        Test1.getUser(user);
//        Thread.sleep(1000* 10);
//        UserService Test2 = ServiceProxyFactory.getProxy(UserService.class);
//        Test1.getUser(user);
        // 测试时 需要注释下方代码
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User newUser = userService.getUser(user);

        if(newUser!= null) {
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }
}
