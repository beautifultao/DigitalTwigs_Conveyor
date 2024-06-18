package com.cumt.data.service;

import java.io.IOException;

public interface ISocketClient {
    boolean connect_socket(String host, int port) throws InterruptedException;
    boolean isConnected();
    void stop_socket() throws IOException;

    /**
     * socket通信的具体内容
     */
    void processData() throws IOException, InterruptedException;
    /**
     * 周期循环通信
     */
    void startPeriodicTask();
}
