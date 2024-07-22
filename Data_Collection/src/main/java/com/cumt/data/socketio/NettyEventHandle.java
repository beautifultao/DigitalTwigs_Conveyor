package com.cumt.data.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.cumt.data.properties.NettyIOProperties;
import com.cumt.data.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NettyEventHandle {
    private final CollectionService collectionService;
    private final NettyIOProperties nettyIOProperties;


    private final ConcurrentHashMap<UUID, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (!socketIOClientMap.containsKey(client.getSessionId())){
            String type = client.getHandshakeData().getSingleUrlParam("type");

            if(type.equals("unity")){
                client.joinRoom(nettyIOProperties.getUnityRoom());
            } else if (type.equals("vue")) {
                client.joinRoom(nettyIOProperties.getVueRoom());
            }

            socketIOClientMap.put(client.getSessionId(),client);
            System.out.println("客户端:" + client.getSessionId() + "已连接");
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

        String type = client.getHandshakeData().getSingleUrlParam("type");
        if(type.equals("unity")){
            client.leaveRoom(nettyIOProperties.getUnityRoom());
        } else if(type.equals("vue")){
            client.leaveRoom(nettyIOProperties.getVueRoom());
        }

        socketIOClientMap.remove(client.getSessionId());
        System.out.println("客户端:" + client.getSessionId() + "已断开连接");

        if(socketIOClientMap.size() == 0){
            if(collectionService != null){
                collectionService.stopCollection();
            }
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
