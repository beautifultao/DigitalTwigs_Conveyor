package com.cumt.data.entity;

import com.cumt.common.entity.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PointCloudResult {
    private List<Vector3> pointCloudList;
    private IntArrayIndex intArrayIndex;
}
