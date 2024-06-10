package com.cumt.calculation.controller;

import com.cumt.calculation.entity.CoalFlow;
import com.cumt.calculation.entity.Vector3;
import com.cumt.calculation.service.ISoLibrary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalculativeDataController {
    private final ISoLibrary _soLibrary;

    @RequestMapping("/calculative/data")
    public CoalFlow getCalculativeCoalFlowData(byte[] rawData){
        return _soLibrary.getCoalFlow(rawData);
    }

    @RequestMapping("/calculative/pointCloud")
    public List<Vector3> getCalculativePointData(byte[] rawData){
        return _soLibrary.getPoints(rawData);
    }
}
