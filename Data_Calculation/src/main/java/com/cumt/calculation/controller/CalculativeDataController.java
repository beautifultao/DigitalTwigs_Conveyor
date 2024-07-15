package com.cumt.calculation.controller;

import com.cumt.calculation.entity.CoalFlow;
import com.cumt.calculation.entity.Vector3;
import com.cumt.calculation.service.ISoLibrary;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculation")
public class CalculativeDataController {
    private final ISoLibrary _soLibrary;

    AtomicInteger counter = new AtomicInteger(1);
    @GetMapping ("/data")
    public CoalFlow getCalculativeCoalFlowData(byte[] rawData){
        return _soLibrary.getCoalFlow(rawData);
    }

    @GetMapping("/pointCloud")
    public List<Vector3> getCalculativePointData(byte[] rawData){
        return _soLibrary.getPoints(rawData);
    }

    @GetMapping("/getImageByte")
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
    }
}
