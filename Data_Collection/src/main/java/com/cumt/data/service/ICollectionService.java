package com.cumt.data.service;

import com.cumt.common.client.CalculativeDataClient;

import java.util.List;

public interface ICollectionService {

    boolean startCollection(CalculativeDataClient client);

    void stopCollection();

    boolean setParam(List<Float> params);

    boolean switchON();

    boolean switchOFF();
}
