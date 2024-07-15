package com.cumt.data.service.Impl;

import com.cumt.common.client.CalculativeDataClient;
import com.cumt.data.service.ICollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CollectionServiceImpl implements ICollectionService {
    private final ReentrantLock lock = new ReentrantLock();

    private Future<?> collectionTask;

    private volatile boolean running = true;

/*    DataPackSo dataPackSo = DataPackSo.INSTANCE;
    DataCenterSo dataCenterSo = DataCenterSo.INSTANCE;*/

    @Autowired
    @Qualifier("taskExecutor")
    private ExecutorService executorService;


    @Override
    public boolean startCollection(CalculativeDataClient client) {
        if(lock.tryLock()) {
            running = true;

            collectionTask = executorService.submit(() -> {
                while (running) {
                    long startTime = System.currentTimeMillis();

                    CompletableFuture.supplyAsync(client::readPhoto/* 提交的任务 */, executorService)
                            .thenAccept(result -> {
                                long duration = System.currentTimeMillis() - startTime;

                                System.out.println("图片大小" + result.length);
                                System.out.println("本轮耗时:" + duration);
                            })
                            .exceptionally(ex -> {
                                log.error("异步线程任务报错：" + ex.getMessage());
                                return null;
                            });

                    try {
                        Thread.sleep(1000);  // 控制轮询间隔
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
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

        if (collectionTask != null) {
            collectionTask.cancel(true);
        }
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
