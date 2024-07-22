package com.cumt.front.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.cumt.common.result.Result;
import com.cumt.front.domain.dto.CameraParamDTO;
import com.cumt.front.domain.dto.PLCParamDTO;
import com.cumt.front.domain.dto.ParamsDTO;
import com.cumt.front.domain.dto.ReconstructionParamDTO;
import com.cumt.front.domain.po.CameraParamPO;
import com.cumt.front.domain.po.PLCParamPO;
import com.cumt.front.domain.po.ReconstructionParamPO;
import com.cumt.front.service.CameraParamService;
import com.cumt.front.service.NacosConfigService;
import com.cumt.front.service.PLCParamService;
import com.cumt.front.service.ReconstructionParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/params")
@RequiredArgsConstructor
public class ParamController {
    private final PLCParamService plcParamService;
    private final CameraParamService cameraParamService;
    private final ReconstructionParamService reconstructParamService;
    private final NacosConfigService nacosConfigService;

    /**
     *   PLC配置相关操作
     **/
    @PostMapping("/setPLCParam")
    public void setPLCParameter(@RequestBody PLCParamDTO plcParamDTO) {
        PLCParamPO plcParamPO = BeanUtil.copyProperties(plcParamDTO, PLCParamPO.class);
        plcParamService.saveParam(plcParamPO);
    }
    @GetMapping("/getPLCParam")
    public Result<List<PLCParamPO>> getPLCParameter() {
        List<PLCParamPO> paramPO = plcParamService.getAllParam();
        return Result.success(paramPO);
    }
    @DeleteMapping("/deletePLCParam/{id}")
    public void deletePLCParamById(@PathVariable Long id) {
        plcParamService.deleteParamById(id);
    }

    /**
     *   网络相机配置相关操作
     **/
    @GetMapping("/getCameraParam")
    public Result<List<CameraParamPO>> getCameraParameter() {
        List<CameraParamPO> paramPO = cameraParamService.getAllParam();
        return Result.success(paramPO);
    }
    @PostMapping("/setCameraParam")
    public void setCameraParameter(@RequestBody CameraParamDTO cameraParamDTO) {
        CameraParamPO cameraParamPO = BeanUtil.copyProperties(cameraParamDTO, CameraParamPO.class);
        cameraParamService.saveParam(cameraParamPO);
    }
    @DeleteMapping("/deleteCameraParam/{id}")
    public void deleteCameraParamById(@PathVariable Long id) {
        cameraParamService.deleteParamById(id);
    }


    /**
     *   三维重建算法配置相关操作
     **/
    @GetMapping("/getReconstructionParam")
    public Result<List<ReconstructionParamPO>> getReconstructionParam(){
        List<ReconstructionParamPO> paramPO = reconstructParamService.getAllParam();
        return Result.success(paramPO);
    }
    @PostMapping("/setReconstructionParam")
    public void setReconstructionParam(@RequestBody ReconstructionParamDTO reconstructParamDTO){
        ReconstructionParamPO reconstructParamPO = BeanUtil.copyProperties(reconstructParamDTO, ReconstructionParamPO.class);
        reconstructParamService.saveParam(reconstructParamPO);
    }
    @DeleteMapping("/deleteReconstructionParam/{id}")
    public void deleteReconstructionParamById(@PathVariable Long id){
        reconstructParamService.deleteParamById(id);
    }

    // TODO: 雷达参数配置、算法参数配置

    @PostMapping("/setParams")
    public Result<String> setParameters(@RequestBody ParamsDTO paramsDTO) {
        // TODO: 接收前端发来的所有自定义配置，更新nacos中的所有配置文件。这个以更新三维重建算法参数为例
        Float gaussianSearchRadius = paramsDTO.getReconstructionParam().getGaussianSearchRadius();
        Float gaussianSigmaWeightX = paramsDTO.getReconstructionParam().getGaussianSigmaWeightX();
        Float gaussianSigmaWeightY = paramsDTO.getReconstructionParam().getGaussianSigmaWeightY();
        Float voxelSize = paramsDTO.getReconstructionParam().getVoxelSize();
        Float distanceThreshold = paramsDTO.getReconstructionParam().getDistanceThreshold();
        Float triangulationSearchRadius = paramsDTO.getReconstructionParam().getTriangulationSearchRadius();
        Integer maxNearestNeighbors = paramsDTO.getReconstructionParam().getMaxNearestNeighbors();

        // 获取nacos中的配置列表
        String dataId = "data-collection.yaml";
        String group = "DEFAULT_GROUP";

        try {
            String content = nacosConfigService.getConfig(dataId, group);

            // 构建完整的 YAML 格式内容
            String updatedContent = content
                    .replaceAll("(?m)^\\s*gaussianSearchRadius:\\s*.*$", "        gaussianSearchRadius: " + gaussianSearchRadius)
                    .replaceAll("(?m)^\\s*gaussianSigmaWeightX:\\s*.*$", "        gaussianSigmaWeightX: " + gaussianSigmaWeightX)
                    .replaceAll("(?m)^\\s*gaussianSigmaWeightY:\\s*.*$", "        gaussianSigmaWeightY: " + gaussianSigmaWeightY)
                    .replaceAll("(?m)^\\s*voxelSize:\\s*.*$", "        voxelSize: " + voxelSize)
                    .replaceAll("(?m)^\\s*distanceThreshold:\\s*.*$", "        distanceThreshold: " + distanceThreshold)
                    .replaceAll("(?m)^\\s*triangulationSearchRadius:\\s*.*$", "        triangulationSearchRadius: " + triangulationSearchRadius)
                    .replaceAll("(?m)^\\s*maxNearestNeighbors:\\s*.*$", "        maxNearestNeighbors: " + maxNearestNeighbors);
            // 推送更新后的配置
            boolean isPublishOk = nacosConfigService.publishConfig(dataId, group, updatedContent);
            return isPublishOk ? Result.success(updatedContent) : Result.error("更新失败");
        }   catch (NacosException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/getParams")
    public Result<String> getParams(){
        String dataId = "data-collection.yaml";
        String group = "DEFAULT_GROUP";

        try {
            String content = nacosConfigService.getConfig(dataId, group);
            return Result.success(content);
        }catch (NacosException e) {
            return Result.error(e.getMessage());
        }
    }
}
