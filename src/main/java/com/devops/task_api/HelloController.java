package com.devops.task_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//this class handles http request and returns data
@RestController
public class HelloController {

    @GetMapping("/api/hello") // maps an http get request to a method
    public String hello(){
        return "Task API is running !";
    }

}
