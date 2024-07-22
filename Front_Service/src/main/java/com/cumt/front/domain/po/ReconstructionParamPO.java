package com.cumt.front.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("reconstruction_param")
public class ReconstructionParamPO implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Float gaussianSearchRadius;
    private Float gaussianSigmaWeightX;
    private Float gaussianSigmaWeightY;
    private Float voxelSize;
    private Float distanceThreshold;
    private Float triangulationSearchRadius;
    private Integer maxNearestNeighbors;

    private LocalDateTime createTime;
}
