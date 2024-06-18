package com.cumt.data.service.Impl.Socket;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.cumt.common.entity.MessageCode;
import com.cumt.common.entity.Vector3;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

@Service("pointsSocketClient")
public class PointsISocketClient extends BaseISocketClient {
    private final ByteBuffer buffer = ByteBuffer.allocate(4080);
    private final byte[] tempBuffer = new byte[4080];
    int bytesRead;
    private final List<Vector3> pointList = new LinkedList<>();
    
    @Resource
    private SocketIOServer socketIOServer;

    /**
     *  获取浮点数点云，解包后发给Unity
     **/
    @Override
    public void processData() {
        if (socketClient.isConnected() && !socketClient.isClosed()){
            try {
                bufferedOut.write(MessageCode.GET_POINT.getCode());
                bufferedOut.flush();

                while ((bytesRead = bufferedIn.read(tempBuffer)) != -1){
                    if (buffer.remaining() < bytesRead) {
                        buffer.compact(); // 准备缓冲区以接收新数据，保留未读数据
                    }

                    // 将读取的数据放入缓冲区
                    buffer.put(tempBuffer, 0, bytesRead);
                    // 切换成读模式
                    buffer.flip();

                    // 寻找数据结束位置
                    int endPosition = findEndPosition(buffer);
                    if(endPosition != -1){
                        buffer.limit(endPosition);
                    }

                    // 处理缓冲区中的数据
                    processBuffer(buffer);

                    // 如果找到数据结束标志，则清空缓冲区并退出循环
                    if (endPosition != -1) {
                        buffer.clear(); // 清空缓冲区，准备下一次接收
                        break;
                    } else {
                        // 没有找到结束标志，继续压缩数据以释放空间
                        buffer.compact();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Client error: " + e.getMessage());
            }
            System.out.println("Received " + pointList.size() + " points.");

            sendPointToUnity();
            pointList.clear(); // 清空列表以接收新的点云数据
            buffer.clear();
        }
    }

    /**
     *  socket通信间隔100ms
     **/
    @Override
    protected int getIntervalSeconds() {
        return 100;
    }


    private void processBuffer(ByteBuffer buffer) {
        while (buffer.remaining() >= Float.BYTES * 3) {
            float x = buffer.getFloat();
            float y = buffer.getFloat();
            float z = buffer.getFloat();
            pointList.add(new Vector3(x, y, z));
        }
    }

    private int findEndPosition(ByteBuffer buffer) {
        for (int i = 0; i < buffer.limit() - 1; i++) {
            if (buffer.get(i) == (byte)'@' && buffer.get(i + 1) == (byte)'@') {
                return i;
            }
        }
        return -1;
    }

    private void sendPointToUnity(){
        BroadcastOperations operations = socketIOServer.getRoomOperations("pointCloud");
        
        if(operations.getClients().size() > 0){
            String pointFrame = JSON.toJSONString(pointList);
            socketIOServer.getRoomOperations("pointCloud").sendEvent("pointReceiving", pointFrame);
        }
    }
}
