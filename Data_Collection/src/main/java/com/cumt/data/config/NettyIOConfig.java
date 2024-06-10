package com.cumt.data.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class NettyIOConfig {
    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.Origin}")
    private String origin;

    @Value("${socketio.bossCount}")
    private Integer bossCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.namespace}")
    private String namespace;

    @Bean
    public SocketIOServer socketIOServer(){
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin(origin);
        config.setBossThreads(bossCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setSocketConfig(socketConfig);

        final SocketIOServer server = new SocketIOServer(config);
        server.addNamespace(namespace);
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

    /**
     * 项目启动时，自动开启socket.io
     */
    @Bean
    public CommandLineRunner run(SocketIOServer server){
        return args -> server.start();
    }
}
