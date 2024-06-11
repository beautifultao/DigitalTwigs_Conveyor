package com.cumt.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "conveyor.socketio")
@Component
@Data
public class NettyIOProperties {
    private String host;
    private Integer port;
    private String origin;
    private Integer bossCount;
    private String[] namespace;
    private boolean allowCustomRequests;
}
