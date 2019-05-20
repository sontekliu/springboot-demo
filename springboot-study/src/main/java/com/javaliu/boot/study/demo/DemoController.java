package com.javaliu.boot.study.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo/")
public class DemoController {

    // @Autowired
    //private StarterService starterService;
    @RequestMapping(value = "hello")
    public String hello(){
        String[] array = {"aa", "bb"};
        for (String str : array){
            System.out.println(str);
        }
        return "Hello World!!!";
    }
}
