package com.atguigu.gulimall.pms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldController {
    @Value("${server.port}")
    private String serverPort;
    @GetMapping("/world")
    public String world(){
        return "world"+"--"+serverPort;
    }
}
