package com.cumt.calculation.controller;

import com.cumt.calculation.service.ReconstructPointService;
import com.cumt.common.entity.PointCloudResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculation")
public class CalculativeDataController {

    private final ReconstructPointService reconstructPointService;

    @GetMapping("/hi")
    public String callServiceB(@RequestParam String param1, @RequestParam String param2) {
        return "接收到参数"+param1+param2;
    }

    @GetMapping("/reconstructIndex")
    PointCloudResult trianglesIndex(@RequestParam byte[] rawData, @RequestParam float z_min,
                                    @RequestParam float z_max){
        return reconstructPointService.getPointListAndIndex(rawData, z_min, z_max);
    }

    // TODO: 数据计算模块暴露给数据服务模块的方法的实现




/*    @GetMapping("/getImageByte")
    public byte[] readPhoto(){
        init();
        return imageBytes;
    }
    private byte[] imageBytes;
    @PostConstruct
    public void init() {
        try {
            // 加载图片文件
            ClassPathResource resource = new ClassPathResource("1.png");
            Path imagePath = resource.getFile().toPath();
            // 读取图片内容到字节数组
            imageBytes = Files.readAllBytes(imagePath);
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }*/
}
