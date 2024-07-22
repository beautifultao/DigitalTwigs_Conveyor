package com.cumt.front.domain.dto;

import lombok.Data;

@Data
public class ReconstructionParamDTO {
    private Float gaussianSearchRadius;
    private Float gaussianSigmaWeightX;
    private Float gaussianSigmaWeightY;
    private Float voxelSize;
    private Float distanceThreshold;
    private Float triangulationSearchRadius;
    private Integer maxNearestNeighbors;
}
