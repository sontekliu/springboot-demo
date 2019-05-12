package com.javaliu.boot.study.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo/")
public class DemoController {

    @RequestMapping(value = "hello")
    public String hello(){
        return "Hello World!!!";
    }
}
