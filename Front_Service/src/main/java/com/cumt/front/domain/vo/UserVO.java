package com.cumt.front.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private String token;
    private Integer privilege;
    private Long remainingTime;
}
