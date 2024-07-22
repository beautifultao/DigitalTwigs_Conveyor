package com.cumt.data.DLL;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface SerializePointCloud extends Library {
    SerializePointCloud INSTANCE = Native.load("SerializePointCloud", SerializePointCloud.class);

    /**
     *
     * @param points: 原始点云数据,
     * @param pointSize: 点的数量
     * @param outputSize: 序列化文件大小
     */
    Pointer generate_and_serialize_point_cloud(Pointer points, int pointSize, IntByReference outputSize);

    /**
     * @param data: 释放动态库中的指针
     */
    void free_serialized_data(Pointer data);
}
