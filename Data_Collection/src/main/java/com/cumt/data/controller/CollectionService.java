package com.cumt.data.controller;

import com.cumt.common.client.CalculativeDataClient;
import com.cumt.common.result.Result;
import com.cumt.data.service.ICollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collection")
public class CollectionService {
    private final CalculativeDataClient calculativeDataClient;
    private final ICollectionService collectionService;

    /**
     * 1. 首先开启线程，循环获取原始数据
     * 2. 将原始数据发送给计算模块获取点云数据和图表数据
     * @return
     */
    @GetMapping("/start")
    public Result startCollection(){
        boolean result = collectionService.startCollection(calculativeDataClient);
        if(result){
            return Result.success();
        }else {
            return Result.success("已开启收集线程");
        }
    }

    @GetMapping("/stop")
    public Result stopCollection(){
        collectionService.stopCollection();
        return Result.success();
    }
}
