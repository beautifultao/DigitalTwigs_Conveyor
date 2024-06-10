package com.cumt.calculation.service;

import com.cumt.calculation.entity.CoalFlow;
import com.cumt.calculation.entity.Vector3;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.List;

public interface ISoLibrary extends Library {
    ISoLibrary INSTANCE = Native.load("Calculation", ISoLibrary.class);

    /**
     *  so库中，将字节数组反序列化为PointCloud对象，内部返回Vector3数组或列表
     */
    List<Vector3> getPoints(byte[] rawPointData);

    /**
     * so库中，返回将字节数组反序列化
     */
    CoalFlow getCoalFlow(byte[] rawData);
}
