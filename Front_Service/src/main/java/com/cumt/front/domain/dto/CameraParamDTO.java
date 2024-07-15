package com.cumt.front.domain.dto;

import lombok.Data;

@Data
public class CameraParamDTO {
    private String username;
    private String password;
    private String ipAddress;
    private int port;
    private String videoType;
    private String channel;
    private String streamType;
}
