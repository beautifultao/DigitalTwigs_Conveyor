package com.cumt.data.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.cumt.common.client.CalculativeDataClient;
import com.cumt.common.entity.Vector3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("CollectionPointServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class CollectionPointServiceImpl extends BaseCollectionService{
    private final CalculativeDataClient calculativeDataClient;
    private final SocketIOServer socketIOServer;

    private final RedisTemplate<String,String> redisTemplate;

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

    /**
     *  (cron = "秒 分 时 日 月 星期 年")
     *  每天0点，12点，18点自动执行CoalFlowToMysql()
     */
    @Scheduled(cron = "0 0 0,9,12,15,18 * * ?")
    public void scheduledCheckAndProcess() {
        List<String> data = redisTemplate.opsForList().range("Conveyor", 0, -1);

        if (data != null && data.size() > 3000) {
            int pageSize = 1000; // 设置每页的大小
            // 存储所有异步任务的 Future
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            // 将数据按页进行分割
            for (int i = 0; i < data.size(); i += pageSize) {
                int end = Math.min(i + pageSize, data.size()); // 确定每页数据的结束索引
                List<String> page = data.subList(i, end); // 获取当前页的数据子列表
                //CompletableFuture<Void> future = CoalFlowToMysql(page); // 异步上传当前页数据到 MySQL
                //futures.add(future); // 将 Future 添加到列表中
            }

            // 等待所有异步任务完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            redisTemplate.delete("Conveyor"); // 删除 Redis 中的 "Conveyor" 列表
        }
    }
}
