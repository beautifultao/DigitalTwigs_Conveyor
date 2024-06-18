package com.cumt.data.controller;

import com.cumt.common.result.Result;
import com.cumt.data.service.ISocketClient;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Socket_Control {
    @Value("${plc_client.ip}")
    String host;
    @Value("${plc_client.port}")
    int port;

    @Resource(name = "pointsSocketClient")
    private ISocketClient ISocketClientPoint;

    @Resource(name = "DataSocketClient")
    private ISocketClient ISocketClientData;

    private Thread th;

    @RequestMapping("/start_socket1")
    public <E>Result<E> start_socket1() throws InterruptedException {
        if(ISocketClientPoint.connect_socket(host, port)){
            th = new Thread(()->{
                ISocketClientPoint.startPeriodicTask();
            });
            th.start();
            return Result.success();
        }else {
            return Result.error("连接失败");
        }
    }

    @RequestMapping("/start_socket2")
    public <E> Result<E> start_socket2() throws InterruptedException {
        if(ISocketClientData.connect_socket(host, 9999)){
            th = new Thread(()->{
                ISocketClientData.startPeriodicTask();
            });
            th.start();
            return Result.success();
        }else {
            return Result.error("连接失败");
        }
    }



    @RequestMapping("/stop_socket1")
    public <E>Result<E> stop_socket1() throws IOException, InterruptedException {
        if(ISocketClientPoint.isConnected()){
            ISocketClientPoint.stop_socket();
            th.join();
            return Result.success();
        } else {
            return Result.error("未连接");
        }
    }
    @RequestMapping("/stop_socket2")
    public <E>Result<E> stop_socket2() throws IOException, InterruptedException {
        if(ISocketClientData.isConnected()){
            ISocketClientData.stop_socket();
            th.join();
            return Result.success();
        } else {
            return Result.error("未连接");
        }
    }
}
