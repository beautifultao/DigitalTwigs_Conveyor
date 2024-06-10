package com.cumt.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.front.entity.po.UserPO;

public interface UserService extends IService<UserPO> {
    UserPO login(String username, String password);

    void register(String username, String password);
}
