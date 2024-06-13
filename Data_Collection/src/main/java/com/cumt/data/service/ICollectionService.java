package com.cumt.data.service;

import com.cumt.common.client.CalculativeDataClient;

public interface ICollectionService {

    boolean startCollection(CalculativeDataClient client);

    void stopCollection();

    boolean startSocket();
    boolean stopSocket();
    boolean isConnected();

    /**
     * 一轮通信的具体内容
     */
    void processData();

    /**
     * 开启循环任务processData()
     */
    void startPeriodTask();
}
