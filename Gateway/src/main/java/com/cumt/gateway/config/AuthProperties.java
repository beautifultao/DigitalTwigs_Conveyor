package com.cumt.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "conveyor.auth")
public class AuthProperties {
    private List<String> excludePaths;
}
