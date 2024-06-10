package com.cumt.common.client;

import com.cumt.common.entity.CoalFlow;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "data-provider")
public interface CoalFlowClient {
    @RequestMapping("/getLatestCoalFlow")
    CoalFlow getCoalFlow();
}
