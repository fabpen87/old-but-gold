package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GreetingController {

    @GetMapping("/api/greet")
    public String greet(HttpServletRequest request) {
        return "Hello from " + request.getRequestURL();
    }
}
