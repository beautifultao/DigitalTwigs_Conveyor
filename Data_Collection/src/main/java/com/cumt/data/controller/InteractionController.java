package com.cumt.data.controller;

import com.cumt.data.properties.NettyIOProperties;
import com.cumt.data.service.BroadcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
public class InteractionController {

    private final BroadcastService broadcastService;
    private final NettyIOProperties nettyIOProperties;

    @GetMapping("/ConveyorON")
    public void conveyorOn() {
        System.out.println("启动皮带");
    }

    @GetMapping("/ConveyorOFF")
    public void conveyorOff() {
        System.out.println("停止皮带");
    }

    @GetMapping("/FeederON")
    public void feederOn() {
        System.out.println("启动输送带");
    }

    @GetMapping("/FeederOFF")
    public void feederOff() {
        System.out.println("停止输送带");
    }

    @PostMapping("/ConveyorSpeed/{speed}")
    public void conveyorSpeed(@PathVariable Float speed) {
        System.out.println("调整皮带速度: " + speed);
        broadcastService.broadcastToRoom(nettyIOProperties.getUnityRoom(),"conveyorSpeed",speed);
    }

    @PostMapping("/FeederSpeed/{speed}")
    public void feederSpeed(@PathVariable Float speed) {
        System.out.println("调整供给物料速度: " + speed);
    }
}
