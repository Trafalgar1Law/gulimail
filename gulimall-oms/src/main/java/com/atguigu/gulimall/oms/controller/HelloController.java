package com.atguigu.gulimall.oms.controller;

import com.atguigu.gulimall.oms.feign.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class HelloController {

    @Value("${url}")
    private String url;

    @Value("${redis.url}")
    private String content;
    @Autowired
    private WorldService worldService;

    @GetMapping("/hello")
    public String hello(){
        String msg="hello";
        msg += worldService.world();
        return msg+"==>"+url+"==>"+content;
    }
}
