package com.cumt.data.service.Impl;

import com.cumt.common.client.CalculativeDataClient;
import com.cumt.data.service.DataCenterSo;
import com.cumt.data.service.DataPackSo;
import com.cumt.data.service.ICollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CollectionServiceImpl implements ICollectionService {
    private final ReentrantLock lock = new ReentrantLock();
    private Thread th;
    private volatile boolean running = true;

    DataPackSo dataPackSo = DataPackSo.INSTANCE;
    DataCenterSo dataCenterSo = DataCenterSo.INSTANCE;

    @Autowired
    @Qualifier("taskExecutor")
    private ExecutorService executorService;


    @Override
    public boolean startCollection(CalculativeDataClient client) {
        if(lock.tryLock()) {
            running = true;

            th = new Thread(()->{
                while (running) {
                    long startTime = System.currentTimeMillis();
                    // 调用taskExecutor线程池，提交异步任务
                    CompletableFuture.supplyAsync(client::readPhoto, executorService)
                           .thenAccept(result -> {
                                long duration = System.currentTimeMillis() - startTime;
                                /**
                                 *  异步任务
                                 */
                                System.out.println("图片大小"+result.length);
                                System.out.println("本轮耗时:"+duration);
                            })
                           .exceptionally(ex -> {
                                log.error("异步线程任务报错："+ex.getMessage());
                                return null;
                            });
                }
            });
            th.start();
            log.info("开始收集数据");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void stopCollection() {
        running = false;
        if(lock.isHeldByCurrentThread())
            lock.unlock();

        th.interrupt();
        log.info("停止收集数据");
    }


    @Override
    public boolean setParam(List<Float> params) {

        return false;
    }

    @Override
    public boolean switchON() {


        return false;
    }

    @Override
    public boolean switchOFF() {
        return false;
    }


}
