package com.cumt.data.service.Impl.Socket;

import com.cumt.data.service.ISocketClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseISocketClient implements ISocketClient {
    protected Socket socketClient;
    protected BufferedOutputStream bufferedOut;
    protected BufferedInputStream bufferedIn;
    protected final ReentrantLock lock = new ReentrantLock();
    private ScheduledExecutorService scheduler;

    public boolean connect_socket(String host, int port) {
        if (lock.tryLock()) {
            try {
                if (socketClient == null || socketClient.isClosed()) {
                    socketClient = new Socket(host, port);
                    bufferedOut = new BufferedOutputStream(socketClient.getOutputStream());
                    bufferedIn = new BufferedInputStream(socketClient.getInputStream());
                    startPeriodicTask();
                    return true;
                }
                return false;
            } catch (IOException e) {
                System.out.println("Failed to start socket: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("获取锁失败");
            return false;
        }
    }

    public boolean isConnected() {
        return socketClient != null && socketClient.isConnected() && !socketClient.isClosed();
    }

    public void stop_socket() throws IOException {
        if(scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        if (socketClient != null && !socketClient.isClosed()) {
            socketClient.close();
            socketClient = null;
            bufferedIn.close();
            bufferedOut.close();
        }

        if(lock.isHeldByCurrentThread())  lock.unlock();
    }

    public void startPeriodicTask() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(this::processData, 0, getIntervalSeconds(), TimeUnit.MILLISECONDS);
        }
    }
    public abstract void processData();

    protected abstract int getIntervalSeconds();
}
