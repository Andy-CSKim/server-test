package com.blockki.spring01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIcontroller {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
