package com.cumt.data.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.cumt.common.client.CalculativeDataClient;
import com.cumt.common.entity.Vector3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CollectionPointServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class CollectionPointServiceImpl extends BaseCollectionService{
    private final CalculativeDataClient calculativeDataClient;
    private final SocketIOServer socketIOServer;

    /**
     * 1.从Collection.so文件中或许PointCloud相关的字节数据
     * 2.发送给Calculation.so文件，将其反序列化为List<Vector3>对象返回
     * 3.并将List<Vector3>对象转换为Json数据
     * 4.若检测到Socketio房间内有客户端，则直接将List<Vector3>的json数据广播到房间内
     */
    @Override
    public void processData() {
        if(super.isConnected()){
            startPeriodTask();
            List<Vector3> pointData = calculativeDataClient.getCalculativePointData(_soLibrary.getPoints());
            sendPointToUnity(pointData);
        }else{
            log.info("Socket未连接");
        }
    }

    @Override
    protected int getIntervalMILLISECONDS() {
        return 20;
    }

    private void sendPointToUnity(List<Vector3> pointList){
        BroadcastOperations operations = socketIOServer.getRoomOperations("pointCloud");

        if(operations.getClients().size() > 0){
            String pointFrame = JSON.toJSONString(pointList);
            socketIOServer.getRoomOperations("pointCloud").sendEvent("pointReceiving", pointFrame);
        }
    }
}
