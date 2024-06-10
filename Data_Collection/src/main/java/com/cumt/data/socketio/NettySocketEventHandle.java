package com.cumt.data.socketio;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnClass(SocketIOClient.class)
public class NettySocketEventHandle {
    private final String roomName = "pointCloud";

    private final ConcurrentHashMap<String,SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();
    private final ArrayList<UUID> clientList = new ArrayList<>();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String unityId = client.getHandshakeData().getSingleUrlParam("unityId");

        if(!clientList.contains(client.getSessionId()) && StrUtil.isNotEmpty(unityId)) {
            clientList.add(client.getSessionId());

            socketIOClientMap.put(unityId,client);

            System.out.println("客户端:" + client.getSessionId() + "已连接, unityId="+unityId);

            client.joinRoom(roomName);
            client.sendEvent("message","hello");
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String unityId = client.getHandshakeData().getSingleUrlParam("unityId");
        if(clientList.contains(client.getSessionId()) && StrUtil.isNotEmpty(unityId)) {

            clientList.remove(client.getSessionId());
            socketIOClientMap.remove(unityId);
        }

        System.out.println("客户端:" + client.getSessionId() + "已断开连接,unityId=" + unityId);
        client.leaveRoom(roomName);
    }

    @OnEvent("message")
    public void onEvent(SocketIOClient client, AckRequest request, String data){
        System.out.println("收到来自客户端[" + client.getSessionId() + "]的消息：" + data);
    }

    @OnEvent("joinRoom")
    public void onJoinRoomEvent(SocketIOClient client, AckRequest request, String roomName){
        client.joinRoom(roomName);
        System.out.println("客户端[" + client.getSessionId() + "]加入房间：" + roomName);
    }
    @OnEvent("leaveRoom")
    public void onLeaveRoomEvent(SocketIOClient client, AckRequest request, String roomName) {
        client.leaveRoom(roomName);
        System.out.println("客户端[" + client.getSessionId() + "]离开房间：" + roomName);
    }
}
