package com.cumt.data.service;

public interface ICollectionService {
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
