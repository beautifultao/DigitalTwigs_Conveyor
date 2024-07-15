package com.cumt.data.socketio;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.cumt.common.entity.Vector3;
import com.cumt.data.DLL.Reconstruct;
import com.cumt.data.entity.IntArrayIndex;
import com.cumt.data.entity.PointCloudResult;
import com.cumt.data.entity.PointCloudWrapper;
import com.cumt.data.service.ICollectionService;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NettyEventHandle {

    @Autowired
    private ICollectionService collectionService;

    private final String roomName = "pointCloud";
    private final ConcurrentHashMap<UUID, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    @OnConnect
    public void onConnect(SocketIOClient client) throws InterruptedException {
        if (!socketIOClientMap.containsKey(client.getSessionId())){
            String type = client.getHandshakeData().getSingleUrlParam("type");

            if(type.equals("unity")){
                client.joinRoom(roomName);
            }

            socketIOClientMap.put(client.getSessionId(),client);
            System.out.println("客户端:" + client.getSessionId() + "已连接");

            List<Vector3> point = ReadPoint("Points/point_14.txt");
            PointCloudResult pointCloudResult = ReStruct2(point);

            String pointList = JSON.toJSONString(pointCloudResult.getPointCloudList());

            for (int i = 0; i < 10; i++) {
                client.sendEvent("pointReceiving",pointList);
                client.sendEvent("index",pointCloudResult.getIntArrayIndex());

                Thread.sleep(100);
            }
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

        String type = client.getHandshakeData().getSingleUrlParam("type");
        if(type.equals("unity")){
            client.leaveRoom(roomName);
        }

        client.sendEvent("event","hello");
        socketIOClientMap.remove(client.getSessionId());
        System.out.println("客户端:" + client.getSessionId() + "已断开连接");

        if(socketIOClientMap.size() == 0){
            collectionService.stopCollection();
        }
    }

    @OnEvent("vue")
    public void onEventOfVue(SocketIOClient client, AckRequest request, String data){
        System.out.println("收到来自客户端[" + client.getSessionId() + "]的消息：" + data);
    }

    @OnEvent("unity")
    public void onEventOfUnity(SocketIOClient client, AckRequest request, String data){
        System.out.println("收到来自客户端[" + client.getSessionId() + "]的消息：" + data);
    }

    private PointCloudResult ReStruct2(List<Vector3> points){
        Pointer processor = Reconstruct.INSTANCE.createProcessor();

        float[] zMinMax = getZMinMax(points);
        int size = points.size();

        Memory memory = new Memory((long) size * 3 * Float.BYTES);

        for (int i = 0; i < size; i++) {
            Vector3 point = points.get(i);
            memory.setFloat((long) i * 3 * Float.BYTES, point.getX());
            memory.setFloat((long) i * 3 * Float.BYTES + Float.BYTES, point.getY());
            memory.setFloat((long) i * 3 * Float.BYTES + 2 * Float.BYTES, point.getZ());
        }


        PointCloudWrapper cloud = Reconstruct.INSTANCE.generatePointCloud(processor, memory, size);


        PointCloudWrapper pre_processing = Reconstruct.INSTANCE.preProcessing(processor, cloud, 0.1f, 0.06f, 0.01f,
                zMinMax[0], 0.01f, 0.006f, zMinMax[1], 3);


        IntByReference point_Size = new IntByReference();
        Pointer processedPointCloud = Reconstruct.INSTANCE.processedPointCloud(processor, pre_processing, point_Size);
        List<Vector3> pointCloudList = pointerToVector3List(processedPointCloud, point_Size.getValue());


        IntByReference outSize = new IntByReference();
        Pointer trianglesPointer = Reconstruct.INSTANCE.greedyProjectionTriangulation(processor, pre_processing, 0.01f, 20, outSize);
        int numTriangles = outSize.getValue();
        int[] triangles = trianglesPointer.getIntArray(0, numTriangles);

        Reconstruct.INSTANCE.destroyProcessor(processor);

        IntArrayIndex index = new IntArrayIndex(triangles);
        return new PointCloudResult(pointCloudList,index);
    }


    private List<Vector3> ReadPoint(String filePath){
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

    private List<Vector3> pointerToVector3List(Pointer pointer, int size) {
        List<Vector3> pointCloudList = new ArrayList<>(size / 3);
        float[] pointArray = pointer.getFloatArray(0, size);

        for (int i = 0; i < size; i += 3) {
            Vector3 point = new Vector3(pointArray[i], pointArray[i + 1], pointArray[i + 2]);
            pointCloudList.add(point);
        }

        return pointCloudList;
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
