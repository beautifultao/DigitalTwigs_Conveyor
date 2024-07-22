package com.cumt.calculation.entity;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;


public class PointCloudWrapper extends Structure {
    public Pointer cloud;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("cloud");
    }
}
