package com.cumt.data.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.cumt.common.client.CalculativeDataClient;
import com.cumt.common.entity.CoalFlow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service("CollectionDataServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class CollectionDataServiceImpl extends BaseCollectionService{

    private final CalculativeDataClient calculativeDataClient;
    private final RedisTemplate<String,String> redisTemplate;

    /**
     * 1.从Collection.so文件中或许CoalFlow相关的字节数据
     * 2.发送给Calculation.so文件，将其反序列化为CoalFlow对象返回
     * 3.并将CoalFlow对象转换为Json数据存储到Redis数据库
     */
    @Override
    public void processData() {
        if(super.isConnected()){
            startPeriodTask();
            CoalFlow coalFlowData = calculativeDataClient.getCalculativeCoalFlowData(_soLibrary.getPLCData());
            redisTemplate.opsForList().leftPush("Conveyor",JSON.toJSONString(coalFlowData));
        }else{
            log.info("Socket未连接");
        }
    }

    @Override
    protected int getIntervalMILLISECONDS() {
        return 1000;
    }
}
