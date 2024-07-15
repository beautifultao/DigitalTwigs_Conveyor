package com.cumt.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.front.domain.dto.LoginDTO;
import com.cumt.front.domain.po.UserPO;
import com.cumt.front.domain.vo.UserVO;

public interface UserService extends IService<UserPO> {
    UserVO login(LoginDTO loginDTO);

    void register(String username, String password);
}
