package com.cumt.front.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.common.utils.JwtUtil;
import com.cumt.front.domain.dto.LoginDTO;
import com.cumt.front.domain.po.UserPO;
import com.cumt.front.domain.vo.UserVO;
import com.cumt.front.mapper.UserMapper;
import com.cumt.front.properties.JwtProperties;
import com.cumt.front.service.UserService;
import io.jsonwebtoken.lang.Assert;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserPO> implements UserService {
    @Resource
    private JwtProperties jwtProperties;
    @Override
    public UserVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        UserPO user = lambdaQuery().eq(UserPO::getUsername, username).one();

        Assert.notNull(user, "用户名不存在");

        //2. 密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        //3.生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getExpiration(), claims);

        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .privilege(user.getPrivilege())
                .remainingTime(jwtProperties.getExpiration())
                .build();
    }

    @Override
    public void register(String username, String password) {
        UserPO user = lambdaQuery().eq(UserPO::getUsername, username).one();

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
