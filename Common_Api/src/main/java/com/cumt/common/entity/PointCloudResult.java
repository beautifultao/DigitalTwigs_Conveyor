package com.cumt.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PointCloudResult {
    private List<Vector3> pointCloudList;
    private IntArrayIndex intArrayIndex;
}
