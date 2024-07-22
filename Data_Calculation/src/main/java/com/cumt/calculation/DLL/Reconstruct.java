package com.cumt.calculation.DLL;

import com.cumt.calculation.entity.PointCloudWrapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface Reconstruct extends Library {
    Reconstruct INSTANCE = Native.load("Reconstruct", Reconstruct.class);

    // 创建PointCloudProcessor实例
    Pointer createProcessor();

    // 销毁PointCloudProcessor实例
    void destroyProcessor(Pointer processor);

    // 生成点云
    PointCloudWrapper deserializePointCloud(Pointer processor,byte[] data, int dataSize);

    // 点云预处理
    PointCloudWrapper preProcessing(Pointer processor, PointCloudWrapper cloud, float search_radius, float sigma_x, float sigma_y,
                                    float z_min_plane_val, float voxel_size, float distance_threshold,
                                    float z_max, int num_arrays);

    // 获取预处理后的点云
    Pointer processedPointCloud(Pointer processor, PointCloudWrapper wrapper, IntByReference out_size);

    // 获取三角网格表面重建索引
    Pointer greedyProjectionTriangulation(Pointer processor, PointCloudWrapper cloud, float search_radius,
                                          int max_nearest_neighbors, IntByReference out_size);
}
