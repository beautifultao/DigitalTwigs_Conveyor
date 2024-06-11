package com.cumt.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.cumt.common.client"})
public class CollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectionApplication.class, args);
    }
}
