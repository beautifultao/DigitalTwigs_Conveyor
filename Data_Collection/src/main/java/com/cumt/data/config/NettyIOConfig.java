package com.cumt.data.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.cumt.data.properties.NettyIOProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;


public class NettyIOConfig {
    @Resource
    private NettyIOProperties socketioProperties;

    @Bean
    public SocketIOServer socketIOServer(){
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);       // 允许小数据包立即发送
        socketConfig.setSoLinger(0);            // 套接字在关闭时立即释放资源

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketioProperties.getHost());
        config.setPort(socketioProperties.getPort());
        config.setOrigin(socketioProperties.getOrigin());
        config.setBossThreads(socketioProperties.getBossCount());
        config.setAllowCustomRequests(socketioProperties.isAllowCustomRequests());
        config.setSocketConfig(socketConfig);

        SocketIOServer server = new SocketIOServer(config);

        server.start();
        //启动socket服务
        return server;
    }

    /**
     * 用于扫描 netty-socketio 注解 比如 @OnConnect、@OnEvent
     */
    @Bean
    public SpringAnnotationScanner getSpringAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }
}
