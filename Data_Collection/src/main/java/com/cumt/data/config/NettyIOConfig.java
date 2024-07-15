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
        config.setHostname(socketioProperties.getHost());   // 自定义socket.IO服务器的host
        config.setPort(socketioProperties.getPort());       // 自定义socket.IO服务器的port
        config.setOrigin(socketioProperties.getOrigin());   // 设置允许的请求来源,*代表允许任意源
        config.setBossThreads(socketioProperties.getBossCount());   // 指定Boss线程的数量
        config.setAllowCustomRequests(socketioProperties.isAllowCustomRequests()); // 是否允许自定义的HTTP请求
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
