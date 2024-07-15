package com.cumt.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cumt.front.domain.po.UserPO;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<UserPO> {
    @Select("select * from user where username = #{username}")
    UserPO selectByUsername(String username);
}
