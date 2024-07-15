package com.cumt.front.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cumt.common.result.Result;
import com.cumt.front.domain.dto.CameraParamDTO;
import com.cumt.front.domain.dto.PLCParamDTO;
import com.cumt.front.domain.dto.ParamsDTO;
import com.cumt.front.domain.po.CameraParamPO;
import com.cumt.front.domain.po.PLCParamPO;
import com.cumt.front.service.CameraParamService;
import com.cumt.front.service.PLCParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/params")
@RequiredArgsConstructor
public class ParamController {
    private final PLCParamService plcParamService;
    private final CameraParamService cameraParamService;

    @PostMapping("/setParams")
    public void setParameters(@RequestBody ParamsDTO paramsDTO) {
        PLCParamDTO plcParamDTO = paramsDTO.getPlcParam();
        CameraParamDTO cameraParamDTO = paramsDTO.getCameraParam();

        PLCParamPO plcParamPO = BeanUtil.copyProperties(plcParamDTO, PLCParamPO.class);
        CameraParamPO cameraParamPO = BeanUtil.copyProperties(cameraParamDTO, CameraParamPO.class);
        plcParamService.saveParam(plcParamPO);

    }

    @PostMapping("/setPLCParam")
    public void setPLCParameter(@RequestBody PLCParamDTO plcParamDTO) {
        PLCParamPO plcParamPO = BeanUtil.copyProperties(plcParamDTO, PLCParamPO.class);
        plcParamService.saveParam(plcParamPO);
    }
    @PostMapping("/setCameraParam")
    public void setCameraParameter(@RequestBody CameraParamDTO cameraParamDTO) {
        CameraParamPO cameraParamPO = BeanUtil.copyProperties(cameraParamDTO, CameraParamPO.class);
        cameraParamService.saveParam(cameraParamPO);
    }

    @GetMapping("/getPLCParam")
    public Result<List<PLCParamPO>> getPLCParameter() {
        List<PLCParamPO> paramPO = plcParamService.getAllParam();

        return Result.success(paramPO);
    }
    @GetMapping("/getCameraParam")
    public Result<List<CameraParamPO>> getCameraParameter() {
        List<CameraParamPO> paramPO = cameraParamService.getAllParam();

        return Result.success(paramPO);
    }

    @DeleteMapping("/deletePLCParam/{id}")
    public void deletePLCParamById(@PathVariable Long id) {
        plcParamService.deleteParamById(id);
    }
    @DeleteMapping("/deleteCameraParam/{id}")
    public void deleteCameraParamById(@PathVariable Long id) {
        cameraParamService.deleteParamById(id);
    }
}
