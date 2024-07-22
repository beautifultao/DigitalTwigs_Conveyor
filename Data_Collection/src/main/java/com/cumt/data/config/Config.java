package com.cumt.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({NettyIOConfig.class,ThreadPoolConfig.class})
public class Config {

}
