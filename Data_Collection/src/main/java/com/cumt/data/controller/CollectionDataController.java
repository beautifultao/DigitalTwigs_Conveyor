package com.cumt.data.controller;

import com.cumt.data.entity.Result;
import com.cumt.data.service.ICollectionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionDataController {


    @Resource(name = "CollectionDataServiceImpl")
    private ICollectionService dataCollectionService;

    @Resource(name = "CollectionPointServiceImpl")
    private ICollectionService pointCollectionService;


    @RequestMapping("/collection/getData")
    Result getCoalFlow(){
        if(dataCollectionService.startSocket()){
            dataCollectionService.startPeriodTask();
            pointCollectionService.startPeriodTask();
            return Result.success();
        }else{
            return Result.error("Socket已启动");
        }
    }


    @RequestMapping("/collection/stopSocket")
    Result stopSocket(){
        if(dataCollectionService.isConnected()){
            dataCollectionService.stopSocket();
            return Result.success();
        }else{
            return Result.error("Socket未连接");
        }
    }
}
