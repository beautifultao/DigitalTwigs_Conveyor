package com.cumt.common.client;

import com.cumt.common.entity.PointCloudResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "data-calculation")  //被调用方的服务名
public interface CalculativeDataClient {

    @GetMapping("/calculation/hi")
    String hello(@RequestParam("param1") String param1, @RequestParam("param2") String param2);

    @GetMapping("/calculation/getImageByte")
    byte[] readPhoto();

    @GetMapping("/calculation/reconstructIndex")
    PointCloudResult trianglesIndex(@RequestParam("rawData") byte[] rawData, @RequestParam("z_min") float z_min,
                                    @RequestParam("z_max") float z_max);

    // TODO: 数据计算模块暴露给数据服务模块的方法。
}
