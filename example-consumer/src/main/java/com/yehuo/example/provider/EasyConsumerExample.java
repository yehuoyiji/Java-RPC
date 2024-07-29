package com.yehuo.example.provider;

import com.yehuo.config.RpcConfig;
import com.yehuo.example.common.model.User;
import com.yehuo.example.common.service.UserService;
import com.yehuo.proxy.ServiceProxyFactory;
import com.yehuo.utils.ConfigUtils;


public class EasyConsumerExample {
    public static void main(String[] args) {

        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc.toString());

        // 静态代理
        // UserService userService = new UserServiceProxy();

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("yehuo");
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
