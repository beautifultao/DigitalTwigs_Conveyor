package com.cumt.front.controller;

import com.cumt.front.properties.JwtProperties;
import com.cumt.common.result.Result;
import com.cumt.common.utils.JwtUtil;
import com.cumt.front.entity.po.UserPO;
import com.cumt.front.entity.vo.UserVO;
import com.cumt.front.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user/")
@RestController
@Slf4j
public class UserController {
    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private UserService userService;

    @GetMapping("/test")
    public Result test(){
        return Result.success("你过关！");
    }

    @PostMapping("/register")
    public Result register(String username, String password){
        log.info("用户注册:{}", username);
        userService.register(username, password);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<UserVO> login(String username, String password){
        log.info("用户登录:{}", username);
        UserPO user = userService.login(username, password);

        // 生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getExpiration(), claims);

        UserVO userLoginVO = UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }


}
