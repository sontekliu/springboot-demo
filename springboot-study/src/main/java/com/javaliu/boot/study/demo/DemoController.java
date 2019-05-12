package com.javaliu.boot.study.demo;

import com.javaliu.boot.starter.demo.StarterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo/")
public class DemoController {

    @Autowired
    private StarterService starterService;
    @RequestMapping(value = "hello")
    public String hello(){
        String[] array = starterService.split(",");
        for (String str : array){
            System.out.println(str);
        }
        return "Hello World!!!";
    }
}
