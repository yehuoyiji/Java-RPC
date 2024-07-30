package com.yehuo.example.provider;

import com.yehuo.example.common.model.User;
import com.yehuo.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
