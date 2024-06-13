package com.cumt.data.service.Impl;

import com.cumt.common.client.CalculativeDataClient;
import com.cumt.data.service.ICollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CollectionServiceImpl implements ICollectionService {

    // 收集原始数据为单线程操作，需要lock限制
    private final ReentrantLock lock = new ReentrantLock();
    private Thread collectionThread;
    private ScheduledExecutorService scheduler;
    private volatile boolean running = true;

    @Autowired
    @Qualifier("taskExecutor")
    private ExecutorService executorService;

    //ICollectionSo collectionSo = ICollectionSo.INSTANCE;

    @Override
    public boolean startCollection(CalculativeDataClient client) {
        if(lock.tryLock()) {
            running = true;
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                if (running) {
                    long startTime = System.currentTimeMillis();
                    CompletableFuture.supplyAsync(client::readPhoto, executorService)
                            .thenAccept(result -> {
                                long duration = System.currentTimeMillis() - startTime;
                                System.out.println("图片大小"+result.length);
                                System.out.println("本轮耗时:"+duration);
                            })
                            .exceptionally(ex -> {
                                System.err.println("Error executing task: " + ex.getMessage());
                                return null;
                            });
                }
            }, 0, 1, TimeUnit.SECONDS);
/*            collectionThread = new Thread(()->{
                while (running) {
                    String result = client.testData();
                    System.out.println("开启线程");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            collectionThread.start();*/
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

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
/*        if(lock.isHeldByCurrentThread())
            lock.unlock();

        if (collectionThread != null && collectionThread.isAlive())
            collectionThread.interrupt();*/
        log.info("停止收集数据");
    }

    @Override
    public boolean startSocket() {

        return false;
    }

    @Override
    public boolean stopSocket() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void processData() {

    }

    @Override
    public void startPeriodTask() {

    }
}
