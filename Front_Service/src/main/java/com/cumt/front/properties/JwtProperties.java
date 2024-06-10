package com.cumt.front.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "conveyor.jwt")
@Data
public class JwtProperties {
    private String secretKey;
    private String tokenHeader;
    private String tokenHead;
    private Long expiration;
}

