package com.cumt.front.service;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Service;

@Service
public class NacosConfigService {

    private final ConfigService configService;

    public NacosConfigService() throws NacosException {
        // TODO: nacos实际的地址需要更改
        this.configService = ConfigFactory.createConfigService("192.168.86.121:8848");
    }

    /**
     *
     * @param dataId: naocs配置文件的dataId
     * @param group: naocs配置文件的group
     * @return: 配置列表中的所有内容
     */
    public String getConfig(String dataId, String group) throws NacosException {
        return configService.getConfig(dataId, group, 5000);
    }

    /**
     *
     * @param content: 要在配置文件中发布的内容
     */
    public boolean publishConfig(String dataId, String group, String content) throws NacosException {
        return configService.publishConfig(dataId, group, content,"yaml");
    }
}
