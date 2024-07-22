package com.cumt.calculation.service;

import com.cumt.calculation.DLL.Reconstruct;
import com.cumt.calculation.entity.PointCloudWrapper;
import com.cumt.common.entity.IntArrayIndex;
import com.cumt.common.entity.PointCloudResult;
import com.cumt.common.entity.Vector3;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReconstructPointService {

    public PointCloudResult getPointListAndIndex(byte[] serializedData, float z_min, float z_max){

        Pointer processor = Reconstruct.INSTANCE.createProcessor();

        // PointCloudWrapper为接收动态库结构体的数据类型
        PointCloudWrapper pointCloudWrapper = Reconstruct.INSTANCE.deserializePointCloud(processor, serializedData, serializedData.length);

        PointCloudWrapper pre_processing = Reconstruct.INSTANCE.preProcessing(processor, pointCloudWrapper, 0.1f, 0.06f, 0.01f,
                z_min, 0.01f, 0.006f, z_max, 3);

        IntByReference point_Size = new IntByReference();
        Pointer processedPointCloud = Reconstruct.INSTANCE.processedPointCloud(processor, pre_processing, point_Size);
        List<Vector3> pointCloudList = pointerToVector3List(processedPointCloud, point_Size.getValue());


        IntByReference outSize = new IntByReference();
        Pointer trianglesPointer = Reconstruct.INSTANCE.greedyProjectionTriangulation(processor, pre_processing, 0.01f, 20, outSize);
        int numTriangles = outSize.getValue();
        int[] triangles = trianglesPointer.getIntArray(0, numTriangles);

        Reconstruct.INSTANCE.destroyProcessor(processor);

        IntArrayIndex index = new IntArrayIndex(triangles);
        return new PointCloudResult(pointCloudList,index);
    }

    private List<Vector3> pointerToVector3List(Pointer pointer, int size) {
        List<Vector3> pointCloudList = new ArrayList<>(size / 3);
        float[] pointArray = pointer.getFloatArray(0, size);

        for (int i = 0; i < size; i += 3) {
            Vector3 point = new Vector3(pointArray[i], pointArray[i + 1], pointArray[i + 2]);
            pointCloudList.add(point);
        }

        return pointCloudList;
    }
}
