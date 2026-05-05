package sn.thiordev221.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/hello")
    public String getMethodName() {
        return "hello world !";
    }
    
    
}
