package com.cumt.data.service.Impl;

import com.cumt.data.service.ICollectionService;
import com.cumt.data.service.ISoLibrary;
import jakarta.annotation.Resource;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public abstract class BaseCollectionService implements ICollectionService {
    @Resource
    protected ISoLibrary _soLibrary;

    protected final ReentrantLock lock = new ReentrantLock();
    private ScheduledExecutorService scheduler;


    public boolean startSocket() {
        if(lock.tryLock()){
            if(_soLibrary.startSocket()){
                return true;
            }else {
                System.out.println("socket开启失败");
                return false;
            }
        }
        else {
            System.out.println("startSocket获取锁失败");
            return false;
        }
    }

    public boolean stopSocket() {
        if(_soLibrary.isConnected()){
            _soLibrary.stopSocket();

            if(lock.isHeldByCurrentThread())
                lock.unlock();
            return true;
        } else {
            System.out.println("stopSocket失败，Socket未连接");
            return false;
        }
    }


    public boolean isConnected() {
        return _soLibrary.isConnected();
    }


    public void startPeriodTask() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(this::processData, 0, getIntervalMILLISECONDS(), TimeUnit.MILLISECONDS);
        }
    }
    public abstract void processData();

    protected abstract int getIntervalMILLISECONDS();
}
