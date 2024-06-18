package com.cumt.data.service;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface DataPackSo extends Library {
    DataPackSo INSTANCE = Native.load("DataPack", DataPackSo.class);

    // hxpVS::PointCloudXYZTSpeed* create_point_cloud_xyzt_speed()
    Pointer create_point_cloud_xyzt_speed();

    // unsigned int cloud_xyzt_get_size(hxpVS::PointCloudXYZTSpeed* cloud)
    int cloud_xyzt_get_size(Pointer cloud);

    // unsigned int cloud_xyzt_serialize(hxpVS::PointCloudXYZTSpeed* cloud, unsigned char *byte_array)
    int cloud_xyzt_serialize(Pointer cloud, Pointer byte_array);

    // unsigned int cloud_xyzt_deserialize(hxpVS::PointCloudXYZTSpeed* cloud, const unsigned char *byte_array, unsigned int point_amount)
    int cloud_xyzt_deserialize(Pointer cloud, Pointer byte_array, int point_amount);


    // hxpVS::PointCloudXYZ* create_point_cloud_xyz()
    Pointer create_point_cloud_xyz();

    // unsigned int cloud_xyz_get_size(hxpVS::PointCloudXYZ* cloud)
    int cloud_xyz_get_size(Pointer cloud);

    // unsigned int cloud_xyz_serialize(hxpVS::PointCloudXYZ* cloud, unsigned char *byte_array)
    int cloud_xyz_serialize(Pointer cloud, Pointer byte_array);

    // unsigned int cloud_xyz_deserialize(hxpVS::PointCloudXYZ* cloud, const unsigned char *byte_array, unsigned int point_amount)
    int cloud_xyz_deserialize(Pointer cloud, Pointer byte_array, int point_amount);
}
