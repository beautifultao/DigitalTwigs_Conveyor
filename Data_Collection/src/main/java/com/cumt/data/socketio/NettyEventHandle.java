package com.cumt.data.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.cumt.data.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnClass(SocketIOClient.class)
public class NettyEventHandle {

    @Autowired
    private ICollectionService collectionService;

    private final String roomName = "pointCloud";
    private final ConcurrentHashMap<UUID, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    @OnConnect
    public void onConnect(SocketIOClient client) {

        String type = client.getHandshakeData().getSingleUrlParam("type");
        if(type.equals("unity")){
            client.joinRoom(roomName);
        }

        socketIOClientMap.put(client.getSessionId(),client);
        System.out.println("客户端:" + client.getSessionId() + "已连接");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

        String type = client.getHandshakeData().getSingleUrlParam("type");
        if(type.equals("unity")){
            client.leaveRoom(roomName);
        }

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
}
