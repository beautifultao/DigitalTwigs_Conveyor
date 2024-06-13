package com.cumt.data.service.Impl.Socket;

import com.alibaba.fastjson2.JSON;
import com.cumt.common.entity.CoalFlow;
import com.cumt.common.entity.MessageCode;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service("DataSocketClient")
public class DataSocketClient extends BaseSocketClient {
    ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * 3).order(ByteOrder.LITTLE_ENDIAN);
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     *  获煤炭取流量、皮带转速等数据
     **/
    @Override
    public void processData() {
        if (socketClient.isConnected() && !socketClient.isClosed()) {
            try {
                bufferedOut.write(MessageCode.GET_DATA.getCode());
                bufferedOut.flush();

                // 接收服务端回传的数据
                int bytesRead = bufferedIn.read(buffer.array());
                if (bytesRead != -1) {
                    buffer.position(bytesRead);
                    buffer.flip();  // 转换为读取模式

                    StoreToRedis(buffer);
                }
                buffer.clear();
            } catch (IOException e){
                System.out.println("Error in sending/receiving data: " + e.getMessage());
            }
        }
    }

    /**
     *  socket通信间隔为1秒
     **/
    @Override
    protected int getIntervalSeconds() {
        return 1000;
    }


    private void StoreToRedis(ByteBuffer buffer){
        if (buffer.remaining() >= Float.BYTES * 3) {
            float currentFlow = buffer.getFloat();
            float sumVolume = buffer.getFloat();
            float conveyorSpeed = buffer.getFloat();

            CoalFlow flow = new CoalFlow(1L,currentFlow,sumVolume,conveyorSpeed,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            redisTemplate.opsForList().leftPush("Conveyor",JSON.toJSONString(flow));
        } else {
            System.out.println("Not enough bytes to read");
        }
    }
}