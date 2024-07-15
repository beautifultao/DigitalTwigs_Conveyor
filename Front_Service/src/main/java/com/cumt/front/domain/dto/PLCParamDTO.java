package com.cumt.front.domain.dto;

import lombok.Data;

@Data
public class PLCParamDTO {
    private String ipAddress;
    private int port;
    private int deviceId;
    private int registerAddress;
}
