package com.cumt.data.SO;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface DataCenterSo extends Library {
    DataCenterSo INSTANCE = Native.load("DataCenter", DataCenterSo.class);

    // DataCenter* create_data_center()
    Pointer create_data_center();

    // bool data_center_set_param(DataCenter* data_center, hxpVS::DataCenterParam* param)
    boolean data_center_set_param(Pointer data_center, Pointer param);

    // bool data_center_switch_on(DataCenter* data_center)
    boolean data_center_switch_on(Pointer data_center);

    // bool data_center_switch_off(DataCenter* data_center)
    boolean data_center_switch_off(Pointer data_center);

    // hxpVS::PointCloudXYZTSpeed* get_raw_data(DataCenter* data_center)
    Pointer get_raw_data(Pointer data_center);
}
