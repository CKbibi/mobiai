package com.cex0.mobiai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping(value = "get")
    public String get(String name) {
        return "hello:" + name;
    }
}
