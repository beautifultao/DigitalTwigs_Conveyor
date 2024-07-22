package com.cumt.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "params.reconstruction")
@Data
public class ParamProperties {
    private Float gaussianSearchRadius;
    private Float gaussianSigmaWeightX;
    private Float gaussianSigmaWeightY;
    private Float voxelSize;
    private Float distanceThreshold;
    private Float triangulationSearchRadius;
    private Integer maxNearestNeighbors;
}
