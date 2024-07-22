package com.cumt.data.service;

import com.alibaba.fastjson2.JSONObject;
import com.cumt.common.client.CalculativeDataClient;
import com.cumt.common.entity.IntArrayIndex;
import com.cumt.common.entity.Vector3;
import com.cumt.data.DLL.SerializePointCloud;
import com.cumt.data.entity.CalculatedData;
import com.cumt.data.properties.NettyIOProperties;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CollectionService {
    private final ReentrantLock lock = new ReentrantLock(); // 只需一个线程来完成获取数据的操作即可
    private Future<?> collectionTask;
    private volatile boolean running = false;

    @Autowired
    @Qualifier("taskExecutor")
    private ExecutorService executorService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private NettyIOProperties nettyIOProperties;

    public String testClient(CalculativeDataClient client){
        return client.hello("hello", "world");
    }

    public void startCollection(CalculativeDataClient client) {
        if(lock.tryLock()) {
            running = true;

            collectionTask = executorService.submit(() -> {
                while (running) {
                    long lastBroadcastTime = System.currentTimeMillis();

                    List<Vector3> pointList = ReadPoint("Points/point_14.txt");
                    byte[] rawData = serializedPointCloud(pointList);
                    float[] zMinMax = getZMinMax(pointList);

                    CompletableFuture.supplyAsync(() -> /*TODO:异步任务的具体内容:获取PLC数据以及原始点云数据*/
                                    client.trianglesIndex(rawData,zMinMax[0],zMinMax[1]), executorService)
                            .thenAccept(result -> {
                                // TODO:处理调用计算模块方法返回的结果result
                                List<Vector3> pointCloudList = result.getPointCloudList();
                                IntArrayIndex index = result.getIntArrayIndex();

                                CalculatedData calculatedData = CalculatedData.builder()
                                        .currentFlow((float) Math.random())
                                        .sumVolume((float) Math.random())
                                        .build();

                                String pointJson = JSONObject.toJSONString(pointCloudList);

                                broadcastService.broadcastToRoom(nettyIOProperties.getUnityRoom(),"pointReceiving",pointJson);
                                broadcastService.broadcastToRoom(nettyIOProperties.getUnityRoom(), "index",index);

                                long currentTime = System.currentTimeMillis();
                                if (currentTime - lastBroadcastTime >= 1000){
                                    broadcastService.broadcastToRoom(nettyIOProperties.getVueRoom(),"lineData",calculatedData.getCurrentFlow());
                                    broadcastService.broadcastToRoom(nettyIOProperties.getVueRoom(),"volumeData",calculatedData.getSumVolume());
                                }
                            })
                            .exceptionally(ex -> {
                                log.error("异步线程任务报错：" + ex.getMessage());
                                return null;
                            });

                    try {
                        Thread.sleep(200);  // 控制轮询间隔
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            log.info("开始收集数据");
        } else {
            log.info("收集数据线程已开启");
        }
    }


    public void stopCollection() {
        running = false;
        if(lock.isHeldByCurrentThread())
            lock.unlock();

        if (collectionTask != null) {
            collectionTask.cancel(true);
        }
        log.info("停止收集数据");
    }


    private byte[] serializedPointCloud(List<Vector3> pointList){
        Memory pointArray = convertPointsToFloatArray(pointList);
        IntByReference outputSize = new IntByReference();

        Pointer serializedData = SerializePointCloud.INSTANCE.generate_and_serialize_point_cloud(pointArray, pointList.size(), outputSize);

        byte[] byteArray = serializedData.getByteArray(0, outputSize.getValue());

        SerializePointCloud.INSTANCE.free_serialized_data(serializedData);
        return byteArray;
    }

    private List<Vector3> ReadPoint(String filePath) {
        List<Vector3> points = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Assuming the columns are separated by spaces or tabs
                if (parts.length == 3) {
                    float x = Float.parseFloat(parts[0]);
                    float y = Float.parseFloat(parts[1]);
                    float z = Float.parseFloat(parts[2]);
                    points.add(new Vector3(x, y, z));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    private Memory convertPointsToFloatArray(List<Vector3> points) {
        int size = points.size();
        Memory memory = new Memory((long) size * 3 * Float.BYTES);

        for (int i = 0; i < size; i++) {
            Vector3 point = points.get(i);
            memory.setFloat((long) i * 3 * Float.BYTES, point.getX());
            memory.setFloat((long) i * 3 * Float.BYTES + Float.BYTES, point.getY());
            memory.setFloat((long) i * 3 * Float.BYTES + 2 * Float.BYTES, point.getZ());
        }
        return memory;
    }

    private float[] getZMinMax(List<Vector3> points) {
        float zMin = Float.MAX_VALUE;
        float zMax = Float.MIN_VALUE;

        for (Vector3 point : points) {
            if (point.getZ() < zMin) {
                zMin = point.getZ();
            }
            if (point.getZ() > zMax) {
                zMax = point.getZ();
            }
        }
        return new float[]{zMin, zMax};
    }
}
