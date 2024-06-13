package com.cumt.data.entity;

import com.alibaba.fastjson2.JSON;
import com.cumt.common.entity.Vector3;

import java.util.ArrayList;
import java.util.List;


public class PointCloudGroup {
    private final List<PointCloudData> pointClouds = new ArrayList<>();
    private final int maxCapacity = 10;

    public void addPointCloud(PointCloudData pointCloudData) {
        PointCloudData cloud = new PointCloudData();
        cloud.setPointsList(pointCloudData.getPointsList());

        if (pointClouds.size() >= maxCapacity) {
            // 如果已经达到最大容量，则移除最早的数据
            pointClouds.remove(0);
        }
        pointClouds.add(cloud);
    }

    public String getLatestPointCloudAsJSON() {
        if (pointClouds.isEmpty()) {
            return "{}";
        }
        List<Vector3> pointsList = pointClouds.get(pointClouds.size() - 1).getPointsList();
        return JSON.toJSONString(pointsList);
    }

    public  List<PointCloudData> getPointClouds() {
        return new ArrayList<>(pointClouds);
    }

    public int getSize(){
        return pointClouds.size();
    }

    public void clearPoints() {
        pointClouds.clear();
    }
}
