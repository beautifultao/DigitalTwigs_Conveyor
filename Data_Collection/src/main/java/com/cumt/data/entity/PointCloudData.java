package com.cumt.data.entity;

import java.util.LinkedList;
import java.util.List;

public class PointCloudData {
    private final List<Vector3> points;

    public PointCloudData(){
        this.points = new LinkedList<>();
    }

    public List<Vector3> getPointsList() {
        return new LinkedList<>(points);
    }

    public void addPoint(Vector3 point) {
        points.add(point);
    }

    public void setPointsList(List<Vector3> newPointsList) {
        points.addAll(newPointsList);
    }

    public int getCount() {
        return points.size();
    }

    public void clearPoints(){
        points.clear();
    }
}
