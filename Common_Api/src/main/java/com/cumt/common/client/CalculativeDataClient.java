package com.cumt.common.client;

import com.cumt.common.entity.CoalFlow;
import com.cumt.common.entity.Vector3;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "data-calculation")
public interface CalculativeDataClient {
    @RequestMapping("/calculative/data")
    CoalFlow getCalculativeCoalFlowData(byte[] rawData);


    @RequestMapping("/calculative/pointCloud")
    List<Vector3> getCalculativePointData(byte[] rawData);
}
