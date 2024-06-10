package com.cumt.front.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserVO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String token;
}
