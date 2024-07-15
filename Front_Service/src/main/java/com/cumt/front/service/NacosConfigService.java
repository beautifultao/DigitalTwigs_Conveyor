package com.cumt.front.service;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Service;

@Service
public class NacosConfigService {

    private final ConfigService configService;

    public NacosConfigService() throws NacosException {
        this.configService = ConfigFactory.createConfigService("192.168.86.121");
    }

    public String getConfig(String dataId, String group) throws NacosException {
        return configService.getConfig(dataId, group, 5000);
    }

    public boolean publishConfig(String dataId, String group, String content) throws NacosException {
        return configService.publishConfig(dataId, group, content);
    }
}
