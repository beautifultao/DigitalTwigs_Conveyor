package com.cumt.front.controller;

import com.cumt.common.result.Result;
import com.cumt.front.domain.dto.LoginDTO;
import com.cumt.front.domain.vo.UserVO;
import com.cumt.front.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    // TODO: 待实现
    @PostMapping("/register")
    public void register(String username, String password){
        log.info("用户注册:{}", username);
        userService.register(username, password);
    }


    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody LoginDTO loginDTO){
        try {
            UserVO userVO = userService.login(loginDTO);
            return Result.success(userVO);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }
}
