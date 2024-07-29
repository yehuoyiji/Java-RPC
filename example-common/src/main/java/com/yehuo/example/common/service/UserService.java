package com.yehuo.example.common.service;

import com.yehuo.example.common.model.User;

public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 新方法 获取一个数字
     * @return
     */
    default short getNumber() {
        return 1;
    }
}
