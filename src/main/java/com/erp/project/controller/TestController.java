package com.erp.project.controller;


import com.erp.project.result.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/test")
    public Payload test(){
        return new Payload("成功");
    }
}
