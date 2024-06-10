package com.cumt.provider.controller;

import com.cumt.provider.domain.CoalFlow;
import com.cumt.provider.service.ICoalFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoalFlowController {
    private final ICoalFlowService coalFlowService;

    @RequestMapping("/getLatestCoalFlow")
    CoalFlow getCoalFlow(){
        return coalFlowService.getLatestFlow();
    }
}
