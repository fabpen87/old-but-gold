package com.example.demo.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/api/greet")
    public String greet(HttpServletRequest request) {
        return "Hello from " + request.getRequestURL();
    }
}
