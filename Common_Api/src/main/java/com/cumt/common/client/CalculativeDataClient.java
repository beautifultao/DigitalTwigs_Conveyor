package com.cumt.common.client;

import com.cumt.common.entity.CoalFlow;
import com.cumt.common.entity.Vector3;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "data-calculation")
public interface CalculativeDataClient {
    @GetMapping ("/calculative/data")
    CoalFlow getCalculativeCoalFlowData(byte[] rawData);

    @GetMapping("/calculative/pointCloud")
    List<Vector3> getCalculativePointData(byte[] rawData);

    @GetMapping("/calculation/getImageByte")
    byte[] readPhoto();
}
