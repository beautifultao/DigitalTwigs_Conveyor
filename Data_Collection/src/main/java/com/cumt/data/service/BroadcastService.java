package com.cumt.data.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BroadcastService {
    private final SocketIOServer socketIOServer;

    /**
     * 向指定房间广播消息
     * @param room 房间名
     * @param event 事件名
     * @param message 消息内容
     */
    public void broadcastToRoom(String room, String event, Object message) {
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(room).getClients();

        if (clients != null && !clients.isEmpty()) {
            // 房间存在且有客户端连接
            socketIOServer.getRoomOperations(room).sendEvent(event, message);
        }
    }
}
