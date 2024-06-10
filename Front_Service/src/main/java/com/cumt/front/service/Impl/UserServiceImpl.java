package com.cumt.front.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.front.entity.po.UserPO;
import com.cumt.front.mapper.UserMapper;
import com.cumt.front.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserPO> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserPO login(String username, String password) {
        //1、根据用户名查询数据库中的数据
        UserPO user = userMapper.selectByUsername(username);

        if(user == null) {
            throw new RuntimeException("用户不存在");
        }

        //2. 密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public void register(String username, String password) {
        UserPO user = userMapper.selectByUsername(username);

        if(user!=null){
            throw new RuntimeException("用户已存在");
        }

        LocalDateTime now = LocalDateTime.now();

        password = DigestUtils.md5DigestAsHex(password.getBytes());
        UserPO userPO = UserPO.builder()
                .username(username)
                .password(password)
                .createTime(now)
                .updateTime(now)
                .build();
        this.save(userPO);
    }
}
