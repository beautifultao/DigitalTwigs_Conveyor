package com.cumt.data.controller;

import com.cumt.common.client.CalculativeDataClient;
import com.cumt.data.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collection")
public class CollectionController {
    private final CalculativeDataClient calculativeDataClient;
    private final CollectionService collectionService;

    @GetMapping("/start")
    public void startCollection(){
        collectionService.startCollection(calculativeDataClient);
    }

    @GetMapping("/stop")
    public void stopCollection(){
        collectionService.stopCollection();
    }

    @GetMapping("/hi")
    public String hi(){
        return collectionService.testClient(calculativeDataClient);
    }
}
