package com.cumt.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({NettyIOConfig.class,RedisConfig.class})
public class Config {

}
